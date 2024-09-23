package org.amoseman.budgetingbackend.application.model;

import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.dao.BucketDAO;
import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;

public class DAOs<C> {
    public final AccountDAO<C> accountDAO;
    public final FinanceRecordDAO<C> financeRecordDAO;
    public final BucketDAO<C> bucketDAO;

    public DAOs(AccountDAO<C> accountDAO, FinanceRecordDAO<C> financeRecordDAO, BucketDAO<C> bucketDAO) {
        this.accountDAO = accountDAO;
        this.financeRecordDAO = financeRecordDAO;
        this.bucketDAO = bucketDAO;
    }
}
