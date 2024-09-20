package org.amoseman.budgetingbackend.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import org.amoseman.budgetingbackend.application.auth.*;
import org.amoseman.budgetingbackend.application.auth.hashing.ArgonHash;
import org.amoseman.budgetingbackend.application.auth.hashing.Hash;
import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.dao.BucketDAO;
import org.amoseman.budgetingbackend.dao.impl.sql.AccountDAOImpl;
import org.amoseman.budgetingbackend.dao.impl.sql.FinanceRecordDAOImpl;
import org.amoseman.budgetingbackend.dao.impl.sql.BucketDAOImpl;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingbackend.exception.mapping.*;
import org.amoseman.budgetingbackend.pojo.account.Account;
import org.amoseman.budgetingbackend.resource.AccountResource;
import org.amoseman.budgetingbackend.resource.FinanceRecordResource;
import org.amoseman.budgetingbackend.resource.BucketResource;
import org.amoseman.budgetingbackend.service.AccountService;
import org.amoseman.budgetingbackend.service.BucketService;
import org.amoseman.budgetingbackend.service.FinanceRecordService;
import org.amoseman.budgetingbackend.service.impl.AccountServiceImpl;
import org.amoseman.budgetingbackend.service.impl.FinanceRecordServiceImpl;
import org.amoseman.budgetingbackend.service.impl.BucketServiceImpl;
import org.amoseman.budgetingbackend.util.Now;
import org.bouncycastle.util.encoders.Base64;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jooq.DSLContext;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * The budgeting website backend application.
 */
public class BudgetingApplication extends Application<BudgetingConfiguration> {
    @Override
    public void run(BudgetingConfiguration configuration, Environment environment) throws Exception {
        SecureRandom random = new SecureRandom();
        Hash hash = new ArgonHash(random, 16, 16, 2, 8192, 1);

        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(configuration.getDatabaseURL());

        AccountDAO<DSLContext> accountDAO = new AccountDAOImpl(connection);
        FinanceRecordDAO<DSLContext> financeRecordDAO = new FinanceRecordDAOImpl(connection);
        BucketDAO<DSLContext> bucketDAO = new BucketDAOImpl(connection);

        AccountService<DSLContext> accountService = new AccountServiceImpl<>(configuration, accountDAO, hash);
        FinanceRecordService<DSLContext> financeRecordService = new FinanceRecordServiceImpl<>(financeRecordDAO);
        BucketService<DSLContext> bucketService =  new BucketServiceImpl<>(bucketDAO, financeRecordDAO);

        registerResources(environment, accountService, financeRecordService, bucketService);
        registerAuth(environment, accountDAO, hash);
        registerExceptionMappers(environment);
        initializeAdminAccount(configuration, hash, accountDAO);
    }

    private void registerResources(Environment environment,
                                   AccountService<DSLContext> accountService,
                                   FinanceRecordService<DSLContext> financeRecordService,
                                   BucketService<DSLContext> bucketService) {
        AccountResource<DSLContext> accountResource = new AccountResource<>(accountService);
        FinanceRecordResource<DSLContext> financeRecordResource = new FinanceRecordResource<>(financeRecordService);
        BucketResource<DSLContext> bucketResource = new BucketResource<>(bucketService);

        environment.jersey().register(accountResource);
        environment.jersey().register(financeRecordResource);
        environment.jersey().register(bucketResource);
    }

    private void registerAuth(Environment environment, AccountDAO<?> accountDAO, Hash hash) {
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(accountDAO, hash))
                .setAuthorizer(new UserAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()
        ));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    private void registerExceptionMappers(Environment environment) {
        environment.jersey().register(new IdentifierAlreadyExistsExceptionMapper());
        environment.jersey().register(new IdentifierDoesNotExistExceptionMapper());
        environment.jersey().register(new NegativeValueExceptionMapper());
        environment.jersey().register(new TotalBucketShareExceededExceptionMapper());
        environment.jersey().register(new DateTimeExceptionMapper());
    }

    private void initializeAdminAccount(BudgetingConfiguration configuration, Hash hash, AccountDAO<?> accountDAO) {
        LocalDateTime now = Now.get();
        byte[] saltBytes = hash.salt();
        String hashString = hash.hash(configuration.getAdminPassword(), saltBytes);
        String salt = Base64.toBase64String(saltBytes);
        Account admin = new Account(configuration.getAdminUsername(), now, now, hashString, salt, Roles.ADMIN);
        try {
            accountDAO.addAccount(admin);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
