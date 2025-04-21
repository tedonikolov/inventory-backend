package bg.tuvarna.models.dto.requests;

import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public class ItemFilter {
    @QueryParam("searchBy")
    private String searchBy;
    @QueryParam("status")
    private ItemStatus status;
    @QueryParam("type")
    private ItemType type;
    @QueryParam("categoryId")
    private Long categoryId;
    @QueryParam("itemsPerPage")
    @DefaultValue("10")
    private Integer itemsPerPage;
    @QueryParam("page")
    @DefaultValue("1")
    private Integer page;

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
