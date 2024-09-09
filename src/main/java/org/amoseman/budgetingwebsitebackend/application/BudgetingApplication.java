package org.amoseman.budgetingwebsitebackend.application;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import org.amoseman.budgetingwebsitebackend.application.auth.User;
import org.amoseman.budgetingwebsitebackend.application.auth.UserAuthenticator;
import org.amoseman.budgetingwebsitebackend.application.auth.UserAuthorizer;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.dao.implementation.sql.SQLAccountDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.DatabaseInitializer;
import org.amoseman.budgetingwebsitebackend.database.implementation.SQLDatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.implementation.SQLDatabaseInitializer;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jooq.DSLContext;

public class BudgetingApplication extends Application<BudgetingConfiguration> {
    @Override
    public void run(BudgetingConfiguration configuration, Environment environment) throws Exception {
        DatabaseConnection<DSLContext> connection = new SQLDatabaseConnection(configuration.getDatabaseURL());
        DatabaseInitializer<DSLContext> initializer = new SQLDatabaseInitializer(connection, configuration);
        initializer.initialize();
        AccountDAO<DSLContext> accountDAO = new SQLAccountDAO(connection);

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(accountDAO))
                .setAuthorizer(new UserAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()
        ));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }
}
