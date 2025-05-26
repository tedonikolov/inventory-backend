package bg.tuvarna.reporsitory;

import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.requests.CardFilter;
import bg.tuvarna.models.entities.Card;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class CardRepository implements PanacheRepository<Card> {
    public boolean isItemFree(Long itemId, LocalDate forDate) {
        return find("item.id = ?1 and (" +
                        "(borrowDate <= ?2 and (returnDate is null or returnDate > ?2)))",
                itemId, forDate).count() == 0;
    }

    public PageListing<Card> findByFilter(CardFilter filter) {
        PanacheQuery<Card> query = find(
                "(:searchBy IS NULL OR :searchBy = '' OR LOWER(concat(item.name, ' ', item.number)) LIKE :searchBy) and " +
                        "(:type IS NULL OR item.type = :type) and " +
                        "(:status IS NULL OR item.status = :status) and " +
                        "(:category_id IS NULL OR item.category.id = :category_id) and " +
                        "(:item_id IS NULL OR item.id = :item_id) and " +
                        "(:department_id IS NULL OR employee.department.id = :department_id) and " +
                        "(:employee_id IS NULL OR employee.id = :employee_id) and " +
                        "(:dateIsNull IS NULL OR (returnDate IS NULL AND :dateIsNull = true) OR (returnDate IS NOT NULL AND :dateIsNull = false)) and " +
                        "(:fromDate IS NULL OR borrowDate >= CAST(:fromDate AS DATE)) and " +
                        "(:toDate IS NULL OR borrowDate <= CAST(:toDate AS DATE))",
                Parameters.with("searchBy", filter.getSearchBy() != null ? "%" + filter.getSearchBy().toLowerCase() + "%" : null)
                        .and("type", filter.getType())
                        .and("status", filter.getStatus())
                        .and("category_id", filter.getCategoryId())
                        .and("item_id", filter.getItemId())
                        .and("department_id", filter.getDepartmentId())
                        .and("employee_id", filter.getEmployeeId())
                        .and("dateIsNull", !filter.isReturned())
                        .and("fromDate", filter.getFromDate())
                        .and("toDate", filter.getToDate()))
                .page(Page.of(filter.getPage() - 1, filter.getItemsPerPage()));

        return new PageListing<Card>(
                query.stream().toList(),
                filter.getPage(),
                filter.getItemsPerPage(),
                query.pageCount()
        );
    }
}
