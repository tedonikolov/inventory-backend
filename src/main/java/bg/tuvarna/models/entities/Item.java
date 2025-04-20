package bg.tuvarna.models.entities;

import bg.tuvarna.enums.ItemStatus;
import bg.tuvarna.enums.ItemType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "items")
public class Item extends PanacheEntity {
    private String number;
    private String name;
    private Double price;
    private Double noAmortizationPrice;
    private Double amortizationPrice;
    private ItemType type;
    private LocalDate acquisition_date;
    private LocalDate exploitation_date;
    private ItemStatus status;
    private Double amortization;
    private LocalDate toDate;
    private LocalDate deregistrationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Card> cards;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getNoAmortizationPrice() {
        return noAmortizationPrice;
    }

    public void setNoAmortizationPrice(Double noAmortizationPrice) {
        this.noAmortizationPrice = noAmortizationPrice;
    }

    public Double getAmortizationPrice() {
        return amortizationPrice;
    }

    public void setAmortizationPrice(Double amortizationPrice) {
        this.amortizationPrice = amortizationPrice;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public LocalDate getAcquisition_date() {
        return acquisition_date;
    }

    public void setAcquisition_date(LocalDate acquisition_date) {
        this.acquisition_date = acquisition_date;
    }

    public LocalDate getExploitation_date() {
        return exploitation_date;
    }

    public void setExploitation_date(LocalDate exploitation_date) {
        this.exploitation_date = exploitation_date;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Double getAmortization() {
        return amortization;
    }

    public void setAmortization(Double amortization) {
        this.amortization = amortization;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public LocalDate getDeregistrationDate() {
        return deregistrationDate;
    }

    public void setDeregistrationDate(LocalDate deregistrationDate) {
        this.deregistrationDate = deregistrationDate;
    }
}