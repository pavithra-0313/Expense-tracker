import java.io.Serializable;
import java.time.LocalDate;

public abstract class Transaction implements Serializable {
    protected double amount;
    protected String category;
    protected LocalDate date;
    private static final long serialVersionUID = 1L;

    public Transaction(double amount, String category, LocalDate date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    
    public abstract void apply(Account account);
}
