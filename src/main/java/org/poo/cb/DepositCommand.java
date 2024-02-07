package org.poo.cb;

public class DepositCommand implements Command{
    private User user;
    private String currency;
    private double amount;

    public DepositCommand(User user, String currency, double amount) {
        this.user = user;
        this.currency = currency;
        this.amount = amount;
    }
    public void execute() {
        user.getPortofoliu().getConturi().get(currency).deposit(amount);
    }
}
