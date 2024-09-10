package org.amoseman.budgetingwebsitebackend.pojo;

public class CreateFinanceEvent {
    private final String type;
    private final long amount;
    private final int year;
    private final int month;
    private final int day;

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
