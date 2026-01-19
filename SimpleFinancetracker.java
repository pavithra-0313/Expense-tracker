
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class SimpleFinanceTracker extends JFrame {
    
    private Account account = new Account();
    

    private JLabel balanceLabel;
    private JTextArea transactionsArea;
    private JTextField amountField;
    private JTextField categoryField;
    private JComboBox<String> typeCombo;
    private JButton addButton;
    private JButton clearButton;
    private JButton reportButton;
    
    public SimpleFinanceTracker() {
        setTitle("Simple Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new BorderLayout(10, 10));
        
       
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
     
        JPanel headerPanel = new JPanel();
        balanceLabel = new JLabel("Balance: $0.00", SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        balanceLabel.setForeground(Color.BLUE);
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(balanceLabel);
        
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Transactions"));
        
        transactionsArea = new JTextArea(15, 40);
        transactionsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        transactionsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionsArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Transaction"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
      
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Type:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        String[] types = {"Income", "Expense"};
        typeCombo = new JComboBox<>(types);
        inputPanel.add(typeCombo, gbc);
        
  
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Amount ($):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        amountField = new JTextField(15);
        inputPanel.add(amountField, gbc);
        
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        categoryField = new JTextField(15);
        inputPanel.add(categoryField, gbc);
        
      
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add Transaction");
        addButton.setBackground(new Color(0, 150, 0));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this::addTransaction);
        
        clearButton = new JButton("Clear All");
        clearButton.setBackground(new Color(200, 0, 0));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> clearTransactions());
        
        reportButton = new JButton("Show Report");
        reportButton.setBackground(new Color(0, 100, 200));
        reportButton.setForeground(Color.WHITE);
        reportButton.addActionListener(e -> showReport());
        
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(reportButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);
        
      
        JPanel quickCatPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        quickCatPanel.setBorder(BorderFactory.createTitledBorder("Quick Categories"));
        
        String[] incomeCats = {"Salary", "Gift", "Investment"};
        String[] expenseCats = {"Food", "Rent", "Transport", "Shopping"};
        
        for (String cat : incomeCats) {
            JButton btn = new JButton(cat);
            btn.setBackground(new Color(220, 255, 220));
            btn.addActionListener(e -> {
                categoryField.setText(cat);
                typeCombo.setSelectedItem("Income");
            });
            quickCatPanel.add(btn);
        }
        
        for (String cat : expenseCats) {
            JButton btn = new JButton(cat);
            btn.setBackground(new Color(255, 220, 220));
            btn.addActionListener(e -> {
                categoryField.setText(cat);
                typeCombo.setSelectedItem("Expense");
            });
            quickCatPanel.add(btn);
        }
        
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
     
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.add(inputPanel, BorderLayout.NORTH);
        bottomWrapper.add(quickCatPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomWrapper, BorderLayout.SOUTH);
        
        add(mainPanel);
        setLocationRelativeTo(null); 
        setVisible(true);
    }
    
    private void addTransaction(ActionEvent e) {
        try {
            String type = (String) typeCombo.getSelectedItem();
            String amountText = amountField.getText().trim();
            String category = categoryField.getText().trim();
            
            if (amountText.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter both amount and category", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Amount must be greater than 0", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Transaction transaction;
            if ("Income".equals(type)) {
                transaction = new Income(amount, category, LocalDate.now());
            } else {
                transaction = new Expense(amount, category, LocalDate.now());
            }
            
            account.addTransaction(transaction);
            
           
            amountField.setText("");
            categoryField.setText("");
            
            updateBalance();
            updateTransactionList();
            
            JOptionPane.showMessageDialog(this, 
                String.format("%s of $%.2f added!", type, amount),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for amount", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBalance() {
        double balance = account.getBalance();
        balanceLabel.setText(String.format("Balance: $%.2f", balance));
        
      
        if (balance < 0) {
            balanceLabel.setForeground(Color.RED);
        } else {
            balanceLabel.setForeground(Color.BLUE);
        }
    }
    
    private void updateTransactionList() {
        StringBuilder sb = new StringBuilder();
        
        for (Transaction t : account.getTransactions()) {
            String type = (t instanceof Income) ? "[INCOME] " : "[EXPENSE]";
            String sign = (t instanceof Income) ? "+" : "-";
            
            sb.append(String.format("%s %s$%.2f - %s (%s)\n",
                type,
                sign,
                t.getAmount(),
                t.getCategory(),
                t.getDate()));
        }
        
        transactionsArea.setText(sb.toString());
        transactionsArea.setCaretPosition(0); 
    }
    
    private void clearTransactions() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Clear all transactions?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            account.clearTransactions();
            updateBalance();
            updateTransactionList();
        }
    }
    
    private void showReport() {
        double totalIncome = 0;
        double totalExpense = 0;
        
        for (Transaction t : account.getTransactions()) {
            if (t instanceof Income) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }
        
        double balance = account.getBalance();
        double savings = totalIncome - totalExpense;
        int count = account.getTransactions().size();
        
        String report = String.format(
            "=== Financial Report ===\n\n" +
            "Total Transactions: %d\n" +
            "Total Income:       $%.2f\n" +
            "Total Expense:      $%.2f\n" +
            "Net Savings:        $%.2f\n" +
            "Current Balance:    $%.2f\n\n" +
            "Income/Expense Ratio: %.1f%%\n",
            count,
            totalIncome,
            totalExpense,
            savings,
            balance,
            totalIncome > 0 ? (totalExpense / totalIncome * 100) : 0
        );
        
        JTextArea reportArea = new JTextArea(report, 12, 30);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);
        
        JOptionPane.showMessageDialog(
            this,
            new JScrollPane(reportArea),
            "Financial Report",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(() -> {
            new SimpleFinanceTracker();
        });
    }
}
