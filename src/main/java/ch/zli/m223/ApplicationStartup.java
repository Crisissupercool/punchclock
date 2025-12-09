package ch.zli.m223;

import ch.zli.m223.service.TestDataService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.logging.Logger;

@ApplicationScoped
public class ApplicationStartup {

    private static final Logger LOG = Logger.getLogger(ApplicationStartup.class.getName());

    @Inject
    TestDataService testDataService;

    @ConfigProperty(name = "app.testdata.load-on-startup", defaultValue = "true")
    boolean loadTestDataOnStartup;

    void onStart(@Observes StartupEvent ev) {
        LOG.info("The application is starting...");

        if (loadTestDataOnStartup) {
            LOG.info("Loading test data on startup (configured via app.testdata.load-on-startup)");
            testDataService.loadTestData();
        } else {
            LOG.info("Test data loading on startup is disabled. Use POST /api/testdata to load manually.");
        }
    }
}
