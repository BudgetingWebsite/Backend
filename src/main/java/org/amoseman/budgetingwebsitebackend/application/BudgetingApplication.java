package org.amoseman.budgetingwebsitebackend.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import org.amoseman.budgetingwebsitebackend.application.auth.Hasher;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.application.auth.UserAuthenticator;
import org.amoseman.budgetingwebsitebackend.application.auth.UserAuthorizer;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.dao.FinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLAccountDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLFinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLPartitionDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.DatabaseInitializer;
import org.amoseman.budgetingwebsitebackend.database.implementation.SQLDatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.implementation.SQLDatabaseInitializer;
import org.amoseman.budgetingwebsitebackend.resource.AccountResource;
import org.amoseman.budgetingwebsitebackend.resource.FinanceEventResource;
import org.amoseman.budgetingwebsitebackend.service.AccountService;
import org.amoseman.budgetingwebsitebackend.service.FinanceEventService;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.security.SecureRandom;

public class BudgetingApplication extends Application<BudgetingConfiguration> {
    @Override
    public void run(BudgetingConfiguration configuration, Environment environment) throws Exception {
        SecureRandom random = new SecureRandom();
        Hasher hasher = new Hasher(random, 16, 16, 2, 8192, 1);

        DatabaseConnection<DSLContext> connection = new SQLDatabaseConnection(configuration.getDatabaseURL());
        DatabaseInitializer<DSLContext> initializer = new SQLDatabaseInitializer(connection, configuration, hasher);
        initializer.initialize();

        AccountDAO<DSLContext> accountDAO = new SQLAccountDAO(connection);
        FinanceEventDAO<DSLContext> financeEventDAO = new SQLFinanceEventDAO(connection);
        PartitionDAO<DSLContext> partitionDAO = new SQLPartitionDAO(connection);

        AccountService<DSLContext> accountService = new AccountService<>(accountDAO, hasher);
        FinanceEventService<DSLContext> financeEventService = new FinanceEventService<>(financeEventDAO);
        // todo: partitionService

        AccountResource<DSLContext> accountResource = new AccountResource<>(accountService);
        FinanceEventResource<DSLContext> financeEventResource = new FinanceEventResource<>(financeEventService);
        // todo: partitionResource

        environment.jersey().register(accountResource);
        environment.jersey().register(financeEventResource);
        // todo: register partitionResource

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(accountDAO, hasher))
                .setAuthorizer(new UserAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()
        ));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }
}
