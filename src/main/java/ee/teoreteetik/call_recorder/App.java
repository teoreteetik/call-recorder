package ee.teoreteetik.call_recorder;

import ee.teoreteetik.call_recorder.endpoint.PlaybackService;
import ee.teoreteetik.call_recorder.endpoint.RecordService;
import java.util.logging.Logger;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

public class App extends ResourceConfig {

    public App() {
        register(TwilioAuthFilter.class);
        register(new LoggingFilter(Logger.getLogger("EndpointLogger"), true));

        register(PlaybackService.class);
        register(RecordService.class);
    }

}