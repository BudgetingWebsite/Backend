package org.amoseman.budgetingbackend.service.impl;

import org.amoseman.budgetingbackend.application.BudgetingConfiguration;
import org.amoseman.budgetingbackend.application.auth.hashing.Hash;
import org.amoseman.budgetingbackend.application.auth.Roles;
import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.exception.AccountAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.AccountDoesNotExistException;
import org.amoseman.budgetingbackend.exception.UsernameExceedsMaxLengthException;
import org.amoseman.budgetingbackend.model.account.Account;
import org.amoseman.budgetingbackend.model.account.op.CreateAccount;
import org.amoseman.budgetingbackend.model.account.op.UpdateAccount;
import org.amoseman.budgetingbackend.password.Result;
import org.amoseman.budgetingbackend.password.ResultType;
import org.amoseman.budgetingbackend.service.AccountService;
import org.amoseman.budgetingbackend.util.Now;
import org.bouncycastle.util.encoders.Base64;

import java.time.LocalDateTime;
import java.util.Optional;


public class AccountServiceImpl<C> extends AccountService<C> {

    public AccountServiceImpl(BudgetingConfiguration configuration, AccountDAO<C> accountDAO, Hash hash) {
        super(configuration, accountDAO, hash);
    }

    @Override
    public ResultType addAccount(CreateAccount usernamePassword) throws AccountAlreadyExistsException, UsernameExceedsMaxLengthException {
        Result result = passwordChecker.check(usernamePassword.getPassword());
        if (result.type != ResultType.SUCCESS) {
            return result.type;
        }

        if (usernamePassword.getUsername().length() > configuration.getMaxUsernameLength()) {
            throw new UsernameExceedsMaxLengthException(configuration.getMaxUsernameLength() , usernamePassword.getUsername());
        }
        byte[] salt = hash.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hash.hash(usernamePassword.getPassword(), salt);
        Account account = new Account(usernamePassword.getUsername(), Now.get(), Now.get(), hash64, salt64, Roles.USER);
        accountDAO.addAccount(account);

        return result.type;
    }

    @Override
    public void removeAccount(String username) throws AccountDoesNotExistException {
        accountDAO.removeAccount(username);
    }

    @Override
    public Optional<Account> getAccount(String username) {
        return accountDAO.getAccount(username);
    }

    @Override
    public ResultType changePassword(String username, String password) throws AccountDoesNotExistException {
        Result result = passwordChecker.check(password);
        if (result.type != ResultType.SUCCESS) {
            return result.type;
        }

        LocalDateTime now = Now.get();
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new AccountDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        byte[] salt = hash.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hash.hash(password, salt);
        UpdateAccount update = new UpdateAccount(username, hash64, salt64, account.roles);
        account = new Account(account, update, now);
        accountDAO.updateAccount(account);

        return result.type;
    }

    @Override
    public void changeRoles(String username, String roles) throws AccountDoesNotExistException {
        LocalDateTime now = Now.get();
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new AccountDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        UpdateAccount update = new UpdateAccount(username, account.hash, account.salt, roles);
        account = new Account(account, update, now);
        accountDAO.updateAccount(account);
    }
}
