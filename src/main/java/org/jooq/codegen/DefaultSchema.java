/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.codegen.tables.Account;
import org.jooq.codegen.tables.Bucket;
import org.jooq.codegen.tables.Expense;
import org.jooq.codegen.tables.Income;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>account</code>.
     */
    public final Account ACCOUNT = Account.ACCOUNT;

    /**
     * The table <code>bucket</code>.
     */
    public final Bucket BUCKET = Bucket.BUCKET;

    /**
     * The table <code>expense</code>.
     */
    public final Expense EXPENSE = Expense.EXPENSE;

    /**
     * The table <code>income</code>.
     */
    public final Income INCOME = Income.INCOME;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Account.ACCOUNT,
            Bucket.BUCKET,
            Expense.EXPENSE,
            Income.INCOME
        );
    }
}
