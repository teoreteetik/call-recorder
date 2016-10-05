package ee.teoreteetik.callRecorder.endpoint;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

import ee.teoreteetik.callRecorder.catena.CatenaClient;
import ee.teoreteetik.callRecorder.TwilioClient;
import ee.teoreteetik.callRecorder.catena.CatenaSignature;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

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
    private final CatenaClient catenaClient;

    public PlaybackService(TwilioClient twilioClient, CatenaClient catenaClient) {
        this.twilioClient = twilioClient;
        this.catenaClient = catenaClient;
    }

    @POST
    public Response playbackCall(@FormParam("From") String recipientNumber,
                                 @FormParam("Body") String fileUrl)
            throws TwiMLException, TwilioRestException, IOException, NoSuchAlgorithmException {

        TwiMLResponse twiml = new TwiMLResponse();
        try {
            Optional<CatenaSignature> catenaSignature = catenaClient.getSignature(fileUrl);
            String encodedPlaybackUrl = getPlaybackUrl(fileUrl, catenaSignature.map(s -> s.aggregationTime));
            twilioClient.makeOutgoingCall(recipientNumber, encodedPlaybackUrl);
        } catch (URISyntaxException e) {
            twilioClient.sendSms(recipientNumber, "Invalid recording url.");
        }
        return Response.ok().entity(twiml.toXML()).build();
    }

    private String getPlaybackUrl(String rawRecordingUrl, Optional<LocalDateTime> aggregationTime) throws URISyntaxException {
        return new URIBuilder("http://twimlets.com/message")
            .addParameter("Message[0]", aggregationTime
                    .map(time -> "Verified by Guardtime. Recording timestamped at "+ time.toString())
                    .orElse("Recording is tampered"))
            .addParameter("Message[1]", "Playing the recorded audio.")
            .addParameter("Message[2]", rawRecordingUrl)
            .addParameter("Message[3]", "End of recording.")
            .build()
            .toASCIIString();
    }
}
