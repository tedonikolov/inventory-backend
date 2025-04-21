package bg.tuvarna.models.dto.requests;

import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public class CardFilter {
    @QueryParam("searchBy")
    private String searchBy;
    @QueryParam("status")
    private ItemStatus status;
    @QueryParam("type")
    private ItemType type;
    @QueryParam("categoryId")
    private Long categoryId;
    @QueryParam("categoryId")
    private Long itemId;
    @QueryParam("employeeId")
    private Long employeeId;
    @QueryParam("itemsPerPage")
    @DefaultValue("10")
    private Integer itemsPerPage;
    @QueryParam("page")
    @DefaultValue("1")
    private Integer page;

    public String getSearchBy() {
        return searchBy;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public ItemType getType() {
        return type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public Integer getPage() {
        return page;
    }
}
