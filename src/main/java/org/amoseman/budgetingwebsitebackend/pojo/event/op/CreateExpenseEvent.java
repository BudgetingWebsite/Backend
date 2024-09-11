package org.amoseman.budgetingwebsitebackend.pojo.event.op;

public class CreateExpenseEvent extends CreateFinanceEvent {
    private String partition;

    public CreateExpenseEvent() {
    }

    public CreateExpenseEvent(long amount, int year, int month, int day, String category, String description, String partition) {
        super("expense", amount, year, month, day, category, description);
        this.partition = partition;
    }

    public String getPartition() {
        return partition;
    }
}
