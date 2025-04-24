package bg.tuvarna.reporsitory;

import bg.tuvarna.models.PageListing;
import bg.tuvarna.models.dto.requests.ItemFilter;
import bg.tuvarna.models.entities.Item;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemRepository implements PanacheRepository<Item> {
    public PageListing<Item> findByFilter(ItemFilter filter) {
        PanacheQuery<Item> query = find(
                "(:searchBy IS NULL OR :searchBy = '' OR LOWER(concat(name, ' ', number)) LIKE :searchBy) and " +
                        "(:type IS NULL OR type = :type) and " +
                        "(:status IS NULL OR status = :status) and " +
                        "(:category_id IS NULL OR category.id = :category_id)",
                Parameters.with("searchBy", filter.getSearchBy() != null ? "%" + filter.getSearchBy().toLowerCase() + "%" : null)
                        .and("type", filter.getType())
                        .and("status", filter.getStatus())
                        .and("category_id", filter.getCategoryId()))
                .page(Page.of(filter.getPage() - 1, filter.getItemsPerPage()));

        return new PageListing<Item>(
                query.stream().toList(),
                filter.getPage(),
                filter.getItemsPerPage(),
                query.pageCount()
        );
    }
}