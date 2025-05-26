package bg.tuvarna.models.entities;

import bg.tuvarna.enums.DepreciationType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends PanacheEntity {
    private String name;
    private DepreciationType depreciationField;
    private Double reductionStep;
    private Double maxAmortizationBeforeScrap;
    private Double maxAmortizationForTypeChange;
    private Integer maxYearsInUse;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Item> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepreciationType getDepreciationField() {
        return depreciationField;
    }

    public void setDepreciationField(DepreciationType depreciation_field) {
        this.depreciationField = depreciation_field;
    }

    public Double getReductionStep() {
        return reductionStep;
    }

    public void setReductionStep(Double reduction_step) {
        this.reductionStep = reduction_step;
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

    public Double getMaxAmortizationForTypeChange() {
        return maxAmortizationForTypeChange;
    }

    public void setMaxAmortizationForTypeChange(Double maxAmortizationForTypeChange) {
        this.maxAmortizationForTypeChange = maxAmortizationForTypeChange;
    }
}
