package bg.tuvarna.models.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity(name = "cards")
public class Card extends PanacheEntity {
    private LocalDate BorrowDate;
    private LocalDate ReturnDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public LocalDate getBorrowDate() {
        return BorrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        BorrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        ReturnDate = returnDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
