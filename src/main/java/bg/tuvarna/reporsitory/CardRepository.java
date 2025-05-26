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
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CardRepository implements PanacheRepository<Card> {
    public boolean isItemFree(Long itemId, LocalDate forDate) {
        return find("item.id = ?1 and (" +
                        "(borrowDate <= ?2 and (returnDate is null or returnDate > ?2)))",
                itemId, forDate).count() == 0;
    }

    public PageListing<Card> findByFilter(CardFilter filter) {
        StringBuilder queryBuilder = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (filter.getSearchBy() != null && !filter.getSearchBy().isEmpty()) {
            queryBuilder.append(" AND LOWER(concat(item.name, ' ', item.number)) LIKE :searchBy");
            params.put("searchBy", "%" + filter.getSearchBy().toLowerCase() + "%");
        }

        if (filter.getType() != null) {
            queryBuilder.append(" AND item.type = :type");
            params.put("type", filter.getType());
        }

        if (filter.getStatus() != null) {
            queryBuilder.append(" AND item.status = :status");
            params.put("status", filter.getStatus());
        }

        if (filter.getCategoryId() != null) {
            queryBuilder.append(" AND item.category.id = :category_id");
            params.put("category_id", filter.getCategoryId());
        }

        if (filter.getItemId() != null) {
            queryBuilder.append(" AND item.id = :item_id");
            params.put("item_id", filter.getItemId());
        }

        if (filter.getDepartmentId() != null) {
            queryBuilder.append(" AND employee.department.id = :department_id");
            params.put("department_id", filter.getDepartmentId());
        }

        if (filter.getEmployeeId() != null) {
            queryBuilder.append(" AND employee.id = :employee_id");
            params.put("employee_id", filter.getEmployeeId());
        }

        if (filter.isReturned() != null) {
            if (filter.isReturned()) {
                queryBuilder.append(" AND returnDate IS NOT NULL");
            } else {
                queryBuilder.append(" AND returnDate IS NULL");
            }
        }

        if (filter.getFromDate() != null) {
            queryBuilder.append(" AND borrowDate >= :fromDate");
            params.put("fromDate", filter.getFromDate());
        }

        if (filter.getToDate() != null) {
            queryBuilder.append(" AND borrowDate <= :toDate");
            params.put("toDate", filter.getToDate());
        }

        PanacheQuery<Card> query = find(queryBuilder.toString(), params)
                .page(Page.of(filter.getPage() - 1, filter.getItemsPerPage()));

        return new PageListing<>(
                query.stream().toList(),
                filter.getPage(),
                filter.getItemsPerPage(),
                query.pageCount()
        );
    }
}
