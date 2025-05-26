package bg.tuvarna.reporsitory;

import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.requests.ItemFilter;
import bg.tuvarna.models.entities.Item;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ItemRepository implements PanacheRepository<Item> {
    public PageListing<Item> findByFilter(ItemFilter filter) {
        StringBuilder queryBuilder = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (filter.getSearchBy() != null && !filter.getSearchBy().isEmpty()) {
            queryBuilder.append(" AND LOWER(concat(name, ' ', number)) LIKE :searchBy");
            params.put("searchBy", "%" + filter.getSearchBy().toLowerCase() + "%");
        }

        if (filter.getType() != null) {
            queryBuilder.append(" AND type = :type");
            params.put("type", filter.getType());
        }

        if (filter.getStatus() != null) {
            queryBuilder.append(" AND status = :status");
            params.put("status", filter.getStatus());
        }

        if (filter.getCategoryId() != null) {
            queryBuilder.append(" AND category.id = :category_id");
            params.put("category_id", filter.getCategoryId());
        }

        if (filter.getFromAcquisitionDate() != null) {
            queryBuilder.append(" AND acquisitionDate >= :fromAcquisitionDate");
            params.put("fromAcquisitionDate", filter.getFromAcquisitionDate());
        }

        if (filter.getToAcquisitionDate() != null) {
            queryBuilder.append(" AND acquisitionDate <= :toAcquisitionDate");
            params.put("toAcquisitionDate", filter.getToAcquisitionDate());
        }

        if (filter.getFromScrapingDate() != null) {
            queryBuilder.append(" AND scrapingDate >= :fromScrapingDate");
            params.put("fromScrapingDate", filter.getFromScrapingDate());
        }

        if (filter.getToScrapingDate() != null) {
            queryBuilder.append(" AND scrapingDate <= :toScrapingDate");
            params.put("toScrapingDate", filter.getToScrapingDate());
        }

        PanacheQuery<Item> query = find(queryBuilder.toString(), params)
                .page(Page.of(filter.getPage() - 1, filter.getItemsPerPage()));

        return new PageListing<>(
                query.stream().toList(),
                filter.getPage(),
                filter.getItemsPerPage(),
                query.pageCount()
        );
    }

    public List<Item> activeItems() {
        return find("type = ?1 and status = ?2", ItemType.DMA, ItemStatus.AVAILABLE).stream().toList();
    }
}