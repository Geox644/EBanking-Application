package org.poo.cb;

public abstract class Account {
    protected String currency;
    protected double balance;


    public Account(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }


    public abstract void deposit(double amount);

    public abstract void withdrawAccount(double amount);

    public abstract void withdrawStocks(double amount);
    public abstract void withdrawAccountPremium(double amount);
    public abstract void withdrawStocksPremium(double amount);
}

class USDAccount extends Account {
    public USDAccount() {
        super("USD");
    }


    @Override
    public void deposit(double amount) {
        balance += amount;
    }


    public void withdrawAccount(double amount) {
        if (balance / 2 < amount) {
            balance = balance - amount - amount * 0.01;
        } else {
            balance -= amount;
        }
    }

    public void withdrawStocks(double amount) {
        balance -= amount;
    }
    public void withdrawAccountPremium(double amount) {
        balance -= amount;
    }
    public void withdrawStocksPremium(double amount) {
        balance = balance - amount;
    }
}

class EURAccount extends Account {
    public EURAccount() {
        super("EUR");
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    public void withdrawAccount(double amount) {
        if (balance / 2 < amount) {
            balance = balance - amount - amount * 0.01;
        } else {
            balance -= amount;
        }
    }

    public void withdrawStocks(double amount) {
        balance -= amount;
    }
    public void withdrawAccountPremium(double amount) {
        balance -= amount;
    }
    public void withdrawStocksPremium(double amount) {
        balance = balance - amount * 0.05;
    }
}

class GBPAccount extends Account {
    public GBPAccount() {
        super("GBP");
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    public void withdrawAccount(double amount) {
        if (balance / 2 < amount) {
            balance = balance - amount - amount * 0.01;
        } else {
            balance -= amount;
        }
    }

    public void withdrawStocks(double amount) {
        balance -= amount;
    }
    public void withdrawAccountPremium(double amount) {
        balance -= amount;
    }
    public void withdrawStocksPremium(double amount) {
        balance -=balance;
    }
}

class JPYAccount extends Account {
    public JPYAccount() {
        super("JPY");
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    public void withdrawAccount(double amount) {
        if (balance / 2 < amount) {
            balance = balance - amount - amount * 0.01;
        } else {
            balance -= amount;
        }
    }

    public void withdrawStocks(double amount) {
        balance -= amount;
    }
    public void withdrawAccountPremium(double amount) {
        balance -= amount;
    }
    public void withdrawStocksPremium(double amount) {
        balance = balance - amount * 0.05;
    }
}

class CADAccount extends Account {
    public CADAccount() {
        super("CAD");
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    public void withdrawAccount(double amount) {
        if (balance / 2 < amount) {
            balance = balance - amount - amount * 0.01;
        } else {
            balance -= amount;
        }
    }

    public void withdrawStocks(double amount) {
        balance -= amount;
    }
    public void withdrawAccountPremium(double amount) {
        balance -= amount;
    }
    public void withdrawStocksPremium(double amount) {
        balance = balance - amount * 0.05;
    }
}
