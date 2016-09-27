package ee.teoreteetik.callRecorder;

import ee.teoreteetik.callRecorder.endpoint.PlaybackService;
import ee.teoreteetik.callRecorder.endpoint.RecordService;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class App extends Application<Config> {

    public static void main(String...args) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(Config config, Environment environment) throws Exception {
        TwilioClient twilioClient = new TwilioClient(config.accountSid, config.authToken, config.smsNumber, config.voiceNumber);

        PlaybackService playbackService = new PlaybackService(twilioClient);
        RecordService recordService = new RecordService(twilioClient);
        TwilioAuthFilter authFilter = new TwilioAuthFilter(config.authToken);

        environment.jersey().register(playbackService);
        environment.jersey().register(recordService);
        //environment.jersey().register(authFilter);

    }

    @Override
    public String getName() {
        return "Call Recorder";
    }
}
