package bg.tuvarna.services;

import bg.tuvarna.enums.DepreciationType;
import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import bg.tuvarna.enums.NotificationType;
import bg.tuvarna.models.dto.ItemDTO;
import bg.tuvarna.models.dto.requests.ItemWithImageDTO;
import bg.tuvarna.models.entities.Card;
import bg.tuvarna.models.entities.Category;
import bg.tuvarna.models.entities.Employee;
import bg.tuvarna.models.entities.Item;
import bg.tuvarna.reporsitory.CategoryRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.services.impl.ItemServiceImpl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemServiceImplTest {

    @Inject
    ItemServiceImpl itemService;

    @Inject
    CategoryRepository categoryRepository;

    @InjectMock
    private NotificationService notificationService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    @Transactional
    void init() {
        Category category = new Category();
        category.setName("Test Category");
        category.setDepreciationField(DepreciationType.LINEAR);
        category.setReductionStep(30.0);
        category.setMaxAmortizationBeforeScrap(80.0);
        category.setMaxAmortizationForTypeChange(70.0);
        category.setMaxYearsInUse(2);
        categoryRepository.persist(category);
        this.category = category;
    }

    @Test
    @Order(1)
    @Transactional
    void save_NewItem_Success(){
        ItemDTO dto = new ItemDTO(null, "1233", "Item", 1250.0, 250.0, 1000.0, ItemType.DMA, LocalDate.now().minusYears(1), LocalDate.now().minusYears(1), ItemStatus.AVAILABLE, 0.0, null, null, 1L, null);
        ItemWithImageDTO request = new ItemWithImageDTO();
        request.setDto(dto);

        itemService.save(request);

        dto = new ItemDTO(null, "1234", "Item", 1250.0, 250.0, 1000.0, ItemType.DMA, LocalDate.now().minusYears(3), LocalDate.now().minusYears(3), ItemStatus.AVAILABLE, 40.0, LocalDate.now().minusYears(1), null, 1L, null);
        request = new ItemWithImageDTO();
        request.setDto(dto);

        itemService.save(request);

        Employee employee = new Employee();
        employee.persist();

        Card card = new Card();
        card.setBorrowDate(LocalDate.now().minusYears(2));
        card.setItem(itemService.findItemById(2L));
        card.setEmployee(employee);
        card.persist();
    }

    @Test
    @Order(2)
    void save_ExistingItemNumber_ThrowsException() {
        ItemDTO dto = new ItemDTO(null, "1233", "Item", 1250.0, 250.0, 1000.0, ItemType.DMA, LocalDate.of(2025,1,1), null, ItemStatus.AVAILABLE, 0.0, null, null, 1L,null);
        ItemWithImageDTO request = new ItemWithImageDTO();

        request.setDto(dto);

        CustomException ex = assertThrows(CustomException.class,() -> itemService.save(request));

        assertEquals("Item already exists", ex.getMessage());
    }

    @Test
    @Order(3)
    void changeAmortization_UpdatesAmortizationForLinearDepreciation() {
        itemService.changeAmortization();
        Item item = itemService.findItemById(1L);

        assertEquals(category.getReductionStep(), item.getAmortization());
        assertEquals(LocalDate.now(), item.getToDate());
    }

    @Test
    @Order(4)
    void transferItemsToMaterial_TransfersEligibleItems() {
        itemService.transferItemsToMaterial();
        Item item = itemService.findItemById(2L);

        assertEquals(ItemType.MA, item.getType());

        verify(notificationService).createNotify(argThat(n -> n.type() == NotificationType.ASSET_TRANSFORMATION));
    }

    @Test
    @Order(5)
    @Transactional
    void performAutomaticScrapping_ScrapsItems_WhenAmortizationExceedsMax() {
        Category category = new Category();
        category.setMaxAmortizationBeforeScrap(50.0);
        category.setMaxYearsInUse(2);

        Employee employee = new Employee();
        employee.persist();

        Item item = new Item();
        item.setType(ItemType.DMA);
        item.setStatus(ItemStatus.AVAILABLE);
        item.setCategory(category);
        item.setAmortization(60.0);
        item.setNumber("item-123");
        item.persist();

        Card card = new Card();
        card.setBorrowDate(LocalDate.now().minusYears(2));
        card.setItem(item);
        card.setEmployee(employee);
        card.persist();
        item.setCards(List.of(card));
        item.persist();

        itemService.performAutomaticScrapping();

        item = itemService.findItemById(item.id);

        assertEquals(ItemStatus.SCRAPED, item.getStatus());
        assertEquals(LocalDate.now(), item.getDeregistrationDate());
        verify(notificationService).createNotify(argThat(n -> n.type() == NotificationType.ASSET_TRANSFORMATION));
    }

    @Test
    @Order(6)
    @Transactional
    void performAutomaticScrapping_ScrapsItems_WhenYearsExceedsMax() {
        Category category = new Category();
        category.setMaxAmortizationBeforeScrap(50.0);
        category.setMaxYearsInUse(2);

        Employee employee = new Employee();
        employee.persist();

        Item item = new Item();
        item.setType(ItemType.DMA);
        item.setStatus(ItemStatus.AVAILABLE);
        item.setCategory(category);
        item.setAmortization(10.0);
        item.setAcquisitionDate(LocalDate.now().minusYears(3));
        item.setNumber("item-123");
        item.persist();

        Card card = new Card();
        card.setBorrowDate(LocalDate.now().minusYears(2));
        card.setItem(item);
        card.setEmployee(employee);
        card.persist();
        item.setCards(List.of(card));
        item.persist();

        itemService.performAutomaticScrapping();

        assertEquals(ItemStatus.SCRAPED, item.getStatus()); // capped at max
        assertEquals(LocalDate.now(), item.getDeregistrationDate()); // capped at max
        verify(notificationService).createNotify(argThat(n -> n.type() == NotificationType.ASSET_TRANSFORMATION));
    }
}
