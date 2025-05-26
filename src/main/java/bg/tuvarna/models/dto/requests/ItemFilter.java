package bg.tuvarna.models.dto.requests;

import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDate;

public class ItemFilter {
    @QueryParam("searchBy")
    private String searchBy;
    @QueryParam("status")
    private ItemStatus status;
    @QueryParam("type")
    private ItemType type;
    @QueryParam("categoryId")
    private Long categoryId;
    @QueryParam("fromAcquisitionDate")
    private LocalDate fromAcquisitionDate;
    @QueryParam("toAcquisitionDate")
    private LocalDate toAcquisitionDate;
    @QueryParam("fromScrapingDate")
    private LocalDate fromScrapingDate;
    @QueryParam("toScrapingDate")
    private LocalDate toScrapingDate;
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

    public LocalDate getFromAcquisitionDate() {
        return fromAcquisitionDate;
    }

    public void setFromAcquisitionDate(LocalDate fromAcquisitionDate) {
        this.fromAcquisitionDate = fromAcquisitionDate;
    }

    public LocalDate getToAcquisitionDate() {
        return toAcquisitionDate;
    }

    public void setToAcquisitionDate(LocalDate toAcquisitionDate) {
        this.toAcquisitionDate = toAcquisitionDate;
    }

    public LocalDate getFromScrapingDate() {
        return fromScrapingDate;
    }

    public void setFromScrapingDate(LocalDate fromScrapingDate) {
        this.fromScrapingDate = fromScrapingDate;
    }

    public LocalDate getToScrapingDate() {
        return toScrapingDate;
    }

    public void setToScrapingDate(LocalDate toScrapingDate) {
        this.toScrapingDate = toScrapingDate;
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
