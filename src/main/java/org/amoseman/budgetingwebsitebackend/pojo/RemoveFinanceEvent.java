package org.amoseman.budgetingwebsitebackend.pojo;

public class RemoveFinanceEvent {
    private final String id;
    private final String type;

    public RemoveFinanceEvent(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
