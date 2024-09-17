package org.amoseman.budgetingwebsitebackend.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import org.amoseman.budgetingwebsitebackend.application.auth.*;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.BucketDAO;
import org.amoseman.budgetingwebsitebackend.dao.impl.sql.SQLAccountDAO;
import org.amoseman.budgetingwebsitebackend.dao.impl.sql.SQLFinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.impl.sql.SQLBucketDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingwebsitebackend.pojo.account.Account;
import org.amoseman.budgetingwebsitebackend.resource.AccountResource;
import org.amoseman.budgetingwebsitebackend.resource.FinanceRecordResource;
import org.amoseman.budgetingwebsitebackend.resource.BucketResource;
import org.amoseman.budgetingwebsitebackend.service.AccountService;
import org.amoseman.budgetingwebsitebackend.service.FinanceRecordService;
import org.amoseman.budgetingwebsitebackend.service.BucketService;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.bouncycastle.util.encoders.Base64;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jooq.DSLContext;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class BudgetingApplication extends Application<BudgetingConfiguration> {
    @Override
    public void run(BudgetingConfiguration configuration, Environment environment) throws Exception {
        SecureRandom random = new SecureRandom();
        Hasher hasher = new Hasher(random, 16, 16, 2, 8192, 1);

        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(configuration.getDatabaseURL());

        AccountDAO<DSLContext> accountDAO = new SQLAccountDAO(connection);
        FinanceRecordDAO<DSLContext> financeRecordDAO = new SQLFinanceRecordDAO(connection);
        BucketDAO<DSLContext> bucketDAO = new SQLBucketDAO(connection);

        AccountService<DSLContext> accountService = new AccountService<>(accountDAO, hasher);
        FinanceRecordService<DSLContext> financeRecordService = new FinanceRecordService<>(financeRecordDAO);
        BucketService<DSLContext> bucketService =  new BucketService<>(bucketDAO, financeRecordDAO);

        AccountResource<DSLContext> accountResource = new AccountResource<>(accountService);
        FinanceRecordResource<DSLContext> financeRecordResource = new FinanceRecordResource<>(financeRecordService);
        BucketResource<DSLContext> bucketResource = new BucketResource<>(bucketService);

        environment.jersey().register(accountResource);
        environment.jersey().register(financeRecordResource);
        environment.jersey().register(bucketResource);

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(accountDAO, hasher))
                .setAuthorizer(new UserAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()
        ));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        LocalDateTime now = Now.get();
        byte[] saltBytes = hasher.salt();
        String hash = hasher.hash(configuration.getAdminPassword(), saltBytes);
        String salt = Base64.toBase64String(saltBytes);
        Account admin = new Account(configuration.getAdminUsername(), now, now, hash, salt, Roles.ADMIN);
        try {
            accountDAO.addAccount(admin);
        }
        catch (Exception e) {

        }
    }
}
