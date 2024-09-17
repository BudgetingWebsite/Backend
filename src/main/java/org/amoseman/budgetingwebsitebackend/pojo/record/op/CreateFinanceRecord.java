package org.amoseman.budgetingwebsitebackend.pojo.record.op;

public class CreateFinanceRecord {
    private long amount;
    private int year;
    private int month;
    private int day;
    private String category;
    private String description;

    public CreateFinanceRecord() {

    }

    public CreateFinanceRecord(long amount, int year, int month, int day, String category, String description) {
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.day = day;
        this.category = category;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
