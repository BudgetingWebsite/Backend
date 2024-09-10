package org.amoseman.budgetingwebsitebackend.pojo;

public class CreateFinanceEvent {
    private String type;
    private long amount;
    private int year;
    private int month;
    private int day;

    public CreateFinanceEvent() {

    }

    public CreateFinanceEvent(String type, long amount, int year, int month, int day) {
        this.type = type;
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public long getAmount() {
        return amount;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
