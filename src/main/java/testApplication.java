


import controller.HelloController;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import model.Person;
import model.Template;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import service.PersonService;

import java.util.Map;

public class testApplication extends Application<testConfiguration> {

    public static void main(final String[] args) throws Exception {
        new testApplication().run(args);
    }



    private final HibernateBundle<testConfiguration> hibernateBundle =
        new HibernateBundle<testConfiguration>(Person.class) {
            @Override
            public DataSourceFactory getDataSourceFactory(testConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<testConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
            new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
            )
        );

        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<testConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(testConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle<testConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(testConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(testConfiguration configuration, Environment environment) {
        final PersonService dao = new PersonService(hibernateBundle.getSessionFactory());
        final Template template = configuration.buildTemplate();

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new HelloController(template));

    }

}
