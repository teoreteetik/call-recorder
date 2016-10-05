package ee.teoreteetik.callRecorder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;

import ee.teoreteetik.callRecorder.catena.CatenaClient;
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
        TwilioClient twilioClient = createTwilioClient(config.twilioConfig);
        CatenaClient catenaClient = createCatenaClient(config.catenaConfig);

        PlaybackService playbackService = new PlaybackService(twilioClient, catenaClient);
        RecordService recordService = new RecordService(twilioClient, catenaClient);
        //TwilioAuthFilter authFilter = new TwilioAuthFilter(config.authToken);

        environment.jersey().register(playbackService);
        environment.jersey().register(recordService);
        //environment.jersey().register(authFilter);
    }

    @Override
    public String getName() {
        return "Call Recorder";
    }

    private TwilioClient createTwilioClient(Config.TwilioConfig config){
        return new TwilioClient(config.accountSid, config.authToken, config.voiceNumber, config.smsNumber);
    }

    private CatenaClient createCatenaClient(Config.CatenaConfig config){
        HttpAuthenticationFeature authFeature = HttpAuthenticationFeature.basic(config.user, config.password);
        ClientConfig clientConfig = new ClientConfig(authFeature);
        Client httpClient = new JerseyClientBuilder().withConfig(clientConfig).build();

        return new CatenaClient(httpClient, config.baseUrl);
    }
}
