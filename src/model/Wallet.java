package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Wallet {
    private String category;
    private double amount;
    private String company;
    private String timeString;
    private Date time;

    public Wallet(String name, double amount, String company, String time) {
        this.category = name;
        this.amount = amount;
        this.company = company;
        this.timeString = time;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.time = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
