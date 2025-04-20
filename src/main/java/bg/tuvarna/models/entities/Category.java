package bg.tuvarna.models.entities;

import bg.tuvarna.enums.DepreciationType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends PanacheEntity {
    private String name;
    private DepreciationType depreciation_field;
    private Double reduction_step;
    private Double maxAmortizationBeforeScrap;
    private Integer maxYearsInUse;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Item> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepreciationType getDepreciation_field() {
        return depreciation_field;
    }

    public void setDepreciation_field(DepreciationType depreciation_field) {
        this.depreciation_field = depreciation_field;
    }

    public Double getReduction_step() {
        return reduction_step;
    }

    public void setReduction_step(Double reduction_step) {
        this.reduction_step = reduction_step;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Double getMaxAmortizationBeforeScrap() {
        return maxAmortizationBeforeScrap;
    }

    public void setMaxAmortizationBeforeScrap(Double maxAmortizationBeforeScrap) {
        this.maxAmortizationBeforeScrap = maxAmortizationBeforeScrap;
    }

    public Integer getMaxYearsInUse() {
        return maxYearsInUse;
    }

    public void setMaxYearsInUse(Integer maxYearsInUse) {
        this.maxYearsInUse = maxYearsInUse;
    }
}
