package model;

public class Wish {
    private String name;
    private double amount;
    private String complete;

    public Wish(String name, double amount) {
        this.name = name;
        this.amount = amount;
        this.complete = "25%";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }
}
