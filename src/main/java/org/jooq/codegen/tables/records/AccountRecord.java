/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.codegen.tables.Account;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AccountRecord extends UpdatableRecordImpl<AccountRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>ACCOUNT.UUID</code>.
     */
    public void setUuid(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>ACCOUNT.UUID</code>.
     */
    public String getUuid() {
        return (String) get(0);
    }

    /**
     * Setter for <code>ACCOUNT.ROLES</code>.
     */
    public void setRoles(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>ACCOUNT.ROLES</code>.
     */
    public String getRoles() {
        return (String) get(1);
    }

    /**
     * Setter for <code>ACCOUNT.HASH</code>.
     */
    public void setHash(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>ACCOUNT.HASH</code>.
     */
    public String getHash() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ACCOUNT.SALT</code>.
     */
    public void setSalt(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>ACCOUNT.SALT</code>.
     */
    public String getSalt() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ACCOUNT.CREATED</code>.
     */
    public void setCreated(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>ACCOUNT.CREATED</code>.
     */
    public LocalDateTime getCreated() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>ACCOUNT.UPDATED</code>.
     */
    public void setUpdated(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>ACCOUNT.UPDATED</code>.
     */
    public LocalDateTime getUpdated() {
        return (LocalDateTime) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AccountRecord
     */
    public AccountRecord() {
        super(Account.ACCOUNT);
    }

    /**
     * Create a detached, initialised AccountRecord
     */
    public AccountRecord(String uuid, String roles, String hash, String salt, LocalDateTime created, LocalDateTime updated) {
        super(Account.ACCOUNT);

        setUuid(uuid);
        setRoles(roles);
        setHash(hash);
        setSalt(salt);
        setCreated(created);
        setUpdated(updated);
        resetChangedOnNotNull();
    }
}
