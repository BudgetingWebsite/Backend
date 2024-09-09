package org.amoseman.budgetingwebsitebackend;

import org.amoseman.budgetingwebsitebackend.pojo.Creatable;

public class Main {
    public static void main(String[] args) {
        Creatable creatable = new Creatable();
        System.out.println(creatable.getCreatedFormatted());
    }
}
