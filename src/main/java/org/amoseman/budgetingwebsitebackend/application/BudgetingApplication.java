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
import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.dao.StatisticsDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLAccountDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLFinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLPartitionDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLStatisticsDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.DatabaseInitializer;
import org.amoseman.budgetingwebsitebackend.database.implementation.SQLDatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.implementation.SQLDatabaseInitializer;
import org.amoseman.budgetingwebsitebackend.resource.AccountResource;
import org.amoseman.budgetingwebsitebackend.resource.FinanceRecordResource;
import org.amoseman.budgetingwebsitebackend.resource.PartitionResource;
import org.amoseman.budgetingwebsitebackend.resource.StatisticsResource;
import org.amoseman.budgetingwebsitebackend.service.AccountService;
import org.amoseman.budgetingwebsitebackend.service.FinanceRecordService;
import org.amoseman.budgetingwebsitebackend.service.PartitionService;
import org.amoseman.budgetingwebsitebackend.service.StatisticsService;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jooq.DSLContext;

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
        FinanceRecordDAO<DSLContext> financeRecordDAO = new SQLFinanceRecordDAO(connection);
        PartitionDAO<DSLContext> partitionDAO = new SQLPartitionDAO(connection);
        StatisticsDAO<DSLContext> statisticsDAO = new SQLStatisticsDAO(connection);

        AccountService<DSLContext> accountService = new AccountService<>(accountDAO, hasher);
        FinanceRecordService<DSLContext> financeRecordService = new FinanceRecordService<>(financeRecordDAO, partitionDAO);
        PartitionService<DSLContext> partitionService =  new PartitionService<>(partitionDAO, financeRecordDAO);
        StatisticsService<DSLContext> statisticsService = new StatisticsService<>(statisticsDAO);

        AccountResource<DSLContext> accountResource = new AccountResource<>(accountService);
        FinanceRecordResource<DSLContext> financeRecordResource = new FinanceRecordResource<>(financeRecordService);
        PartitionResource<DSLContext> partitionResource = new PartitionResource<>(partitionService);
        StatisticsResource<DSLContext> statisticsResource = new StatisticsResource<>(statisticsService);

        environment.jersey().register(accountResource);
        environment.jersey().register(financeRecordResource);
        environment.jersey().register(partitionResource);
        environment.jersey().register(statisticsResource);

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
