/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.codegen.DefaultSchema;
import org.jooq.codegen.Keys;
import org.jooq.codegen.tables.Account.AccountPath;
import org.jooq.codegen.tables.Expense.ExpensePath;
import org.jooq.codegen.tables.records.PartitionRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Partition extends TableImpl<PartitionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>partition</code>
     */
    public static final Partition PARTITION = new Partition();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PartitionRecord> getRecordType() {
        return PartitionRecord.class;
    }

    /**
     * The column <code>partition.uuid</code>.
     */
    public final TableField<PartitionRecord, String> UUID = createField(DSL.name("uuid"), SQLDataType.VARCHAR(1000), this, "");

    /**
     * The column <code>partition.owner</code>.
     */
    public final TableField<PartitionRecord, String> OWNER = createField(DSL.name("owner"), SQLDataType.VARCHAR(1000), this, "");

    /**
     * The column <code>partition.name</code>.
     */
    public final TableField<PartitionRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(1000), this, "");

    /**
     * The column <code>partition.share</code>.
     */
    public final TableField<PartitionRecord, Double> SHARE = createField(DSL.name("share"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>partition.created</code>.
     */
    public final TableField<PartitionRecord, LocalDateTime> CREATED = createField(DSL.name("created"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

    /**
     * The column <code>partition.updated</code>.
     */
    public final TableField<PartitionRecord, LocalDateTime> UPDATED = createField(DSL.name("updated"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

    private Partition(Name alias, Table<PartitionRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Partition(Name alias, Table<PartitionRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>partition</code> table reference
     */
    public Partition(String alias) {
        this(DSL.name(alias), PARTITION);
    }

    /**
     * Create an aliased <code>partition</code> table reference
     */
    public Partition(Name alias) {
        this(alias, PARTITION);
    }

    /**
     * Create a <code>partition</code> table reference
     */
    public Partition() {
        this(DSL.name("partition"), null);
    }

    public <O extends Record> Partition(Table<O> path, ForeignKey<O, PartitionRecord> childPath, InverseForeignKey<O, PartitionRecord> parentPath) {
        super(path, childPath, parentPath, PARTITION);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class PartitionPath extends Partition implements Path<PartitionRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> PartitionPath(Table<O> path, ForeignKey<O, PartitionRecord> childPath, InverseForeignKey<O, PartitionRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private PartitionPath(Name alias, Table<PartitionRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public PartitionPath as(String alias) {
            return new PartitionPath(DSL.name(alias), this);
        }

        @Override
        public PartitionPath as(Name alias) {
            return new PartitionPath(alias, this);
        }

        @Override
        public PartitionPath as(Table<?> alias) {
            return new PartitionPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<PartitionRecord> getPrimaryKey() {
        return Keys.PARTITION__PK_PARTITION;
    }

    @Override
    public List<ForeignKey<PartitionRecord, ?>> getReferences() {
        return Arrays.asList(Keys.PARTITION__FK_PARTITION_PK_ACCOUNT);
    }

    private transient AccountPath _account;

    /**
     * Get the implicit join path to the <code>account</code> table.
     */
    public AccountPath account() {
        if (_account == null)
            _account = new AccountPath(this, Keys.PARTITION__FK_PARTITION_PK_ACCOUNT, null);

        return _account;
    }

    private transient ExpensePath _expense;

    /**
     * Get the implicit to-many join path to the <code>expense</code> table
     */
    public ExpensePath expense() {
        if (_expense == null)
            _expense = new ExpensePath(this, null, Keys.EXPENSE__FK_EXPENSE_PK_PARTITION.getInverseKey());

        return _expense;
    }

    @Override
    public Partition as(String alias) {
        return new Partition(DSL.name(alias), this);
    }

    @Override
    public Partition as(Name alias) {
        return new Partition(alias, this);
    }

    @Override
    public Partition as(Table<?> alias) {
        return new Partition(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Partition rename(String name) {
        return new Partition(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Partition rename(Name name) {
        return new Partition(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Partition rename(Table<?> name) {
        return new Partition(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Partition where(Condition condition) {
        return new Partition(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Partition where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Partition where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Partition where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Partition where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Partition where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Partition where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Partition where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Partition whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Partition whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}