package org.amoseman.budgetingbackend.application.model;

import org.amoseman.budgetingbackend.service.AccountService;
import org.amoseman.budgetingbackend.service.BucketService;
import org.amoseman.budgetingbackend.service.FinanceRecordService;

public class Services<C> {
    public final AccountService<C> accountService;
    public final FinanceRecordService<C> financeRecordService;
    public final BucketService<C> bucketService;

    public Services(AccountService<C> accountService, FinanceRecordService<C> financeRecordService, BucketService<C> bucketService) {
        this.accountService = accountService;
        this.financeRecordService = financeRecordService;
        this.bucketService = bucketService;
    }
}
