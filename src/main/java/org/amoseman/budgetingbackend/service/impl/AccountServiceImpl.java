package org.amoseman.budgetingbackend.service.impl;

import org.amoseman.budgetingbackend.application.BudgetingConfiguration;
import org.amoseman.budgetingbackend.application.auth.hashing.Hash;
import org.amoseman.budgetingbackend.application.auth.Roles;
import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.exception.AccountAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.AccountDoesNotExistException;
import org.amoseman.budgetingbackend.exception.UsernameExceedsMaxLengthException;
import org.amoseman.budgetingbackend.exception.InvalidPasswordException;
import org.amoseman.budgetingbackend.model.account.Account;
import org.amoseman.budgetingbackend.model.account.op.CreateAccount;
import org.amoseman.budgetingbackend.model.account.op.UpdateAccount;
import org.amoseman.budgetingbackend.password.PasswordValidationResult;
import org.amoseman.budgetingbackend.password.PasswordValidationType;
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
    public void addAccount(CreateAccount usernamePassword) throws AccountAlreadyExistsException, UsernameExceedsMaxLengthException, InvalidPasswordException {
        PasswordValidationResult passwordValidationResult = passwordValidator.check(usernamePassword.getPassword());
        if (passwordValidationResult.type != PasswordValidationType.SUCCESS) {
            throw passwordValidationResult.asException();
        }

        if (usernamePassword.getUsername().length() > configuration.getMaxUsernameLength()) {
            throw new UsernameExceedsMaxLengthException(configuration.getMaxUsernameLength() , usernamePassword.getUsername());
        }
        byte[] salt = hash.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hash.hash(usernamePassword.getPassword(), salt);
        Account account = new Account(usernamePassword.getUsername(), Now.get(), Now.get(), hash64, salt64, Roles.USER);
        accountDAO.addAccount(account);
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
    public void changePassword(String username, String password) throws AccountDoesNotExistException, InvalidPasswordException {
        PasswordValidationResult passwordValidationResult = passwordValidator.check(password);
        if (passwordValidationResult.type != PasswordValidationType.SUCCESS) {
            throw passwordValidationResult.asException();
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
