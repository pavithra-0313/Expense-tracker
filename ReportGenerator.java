public class ReportGenerator {

    public void generateReport(Account account) {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : account.getTransactions()) {
            if (t instanceof Income) {
                totalIncome += t.getAmount();
            } else if (t instanceof Expense) {
                totalExpense += t.getAmount();
            }
        }

        double savings = totalIncome - totalExpense;

        System.out.println("----- Financial Report -----");
        System.out.println("Total Income   : " + totalIncome);
        System.out.println("Total Expense  : " + totalExpense);
        System.out.println("Net Savings    : " + savings);
        System.out.println("Current Balance: " + account.getBalance());
    }
}
