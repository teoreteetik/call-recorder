package ee.teoreteetik.callRecorder.endpoint;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import ee.teoreteetik.callRecorder.TwilioClient;
import java.net.URISyntaxException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.client.utils.URIBuilder;

@Path("/callPlayback")
@Produces(MediaType.APPLICATION_XML)
public class PlaybackService {

    private final TwilioClient twilioClient;

    public PlaybackService(TwilioClient twilioClient) {
        this.twilioClient = twilioClient;
    }

    @POST
    public Response playbackCall(@FormParam("From") String recipientNumber,
                                 @FormParam("Body") String smsBody) throws TwiMLException, TwilioRestException {

        TwiMLResponse twiml = new TwiMLResponse();
        try {
            String encodedPlaybackUrl = getEncodedPlaybackUrl(smsBody);
            twilioClient.makeOutgoingCall(recipientNumber, encodedPlaybackUrl);
        } catch (URISyntaxException e) {
            twilioClient.sendSms(recipientNumber, "Invalid recording url.");
        }
        return Response.ok().entity(twiml.toXML()).build();
    }

    private String getEncodedPlaybackUrl(String rawRecordingUrl) throws URISyntaxException {
        return new URIBuilder("http://twimlets.com/message")
            .addParameter("Message[0]", "Playing the recorded audio.")
            .addParameter("Message[1]", rawRecordingUrl)
            .addParameter("Message[2]", "End of recording.")
            .build()
            .toASCIIString();
    }

}
