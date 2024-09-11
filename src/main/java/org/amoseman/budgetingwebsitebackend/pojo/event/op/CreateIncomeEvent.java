package org.amoseman.budgetingwebsitebackend.pojo.event.op;

public class CreateIncomeEvent extends CreateFinanceEvent {

    public CreateIncomeEvent() {
    }

    public CreateIncomeEvent(long amount, int year, int month, int day, String category, String description) {
        super("income", amount, year, month, day, category, description);
    }
}
