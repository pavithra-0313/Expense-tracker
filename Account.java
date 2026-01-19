import java.util.ArrayList;
import java.util.List;

public class Account {
    private double balance;
    private List<Transaction> transactions;

    public Account() {
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    
    protected void increaseBalance(double amount) {
        balance += amount;
    }

    protected void decreaseBalance(double amount) {
        balance -= amount;
    }

    public void addTransaction(Transaction transaction) {
        transaction.apply(this);   
        transactions.add(transaction);
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    
    public void clearTransactions() {
        this.transactions.clear();
        this.balance = 0.0;
    }
}
