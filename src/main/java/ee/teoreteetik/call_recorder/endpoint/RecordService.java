package ee.teoreteetik.call_recorder.endpoint;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.*;
import com.twilio.sdk.verbs.Number;
import ee.teoreteetik.call_recorder.TwilioClient;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/recordCall")
@Produces(MediaType.APPLICATION_XML)
public class RecordService {

    @POST
    @Path("/receive")
    public Response receive() throws TwiMLException {
        TwiMLResponse twiml = new TwiMLResponse();

        Gather gather = new Gather();
        gather.setFinishOnKey("*");
        gather.setTimeout(10);
        gather.setAction("forward");
        gather.append(new Say("Enter the phone number you wish to call ending with an asterisk."));
        twiml.append(gather);

        return Response.ok().entity(twiml.toXML()).build();
    }

    @POST
    @Path("/forward")
    public Response forward(@FormParam("Digits") String digits, @Context HttpServletRequest sc) throws TwiMLException {
        TwiMLResponse twiml = new TwiMLResponse();
        twiml.append(new Say("Forwarding the call, please stand by."));

        Dial dial = new Dial();
        dial.set("record", "record-from-answer");
        dial.setAction("send");
        dial.setTimeout(120);
        Number recipientNumber = new Number(digits);
        recipientNumber.setUrl("confirm");
        dial.append(recipientNumber);
        twiml.append(dial);

        return Response.ok().entity(twiml.toXML()).build();
    }

    @POST
    @Path("/confirm")
    public Response confirm() throws TwiMLException {
        TwiMLResponse twiml = new TwiMLResponse();
        twiml.append(new Say("This call will be recorded. You will be connected in 5 seconds."));

        Pause pause = new Pause();
        pause.setLength(5);
        twiml.append(pause);

        return Response.ok().entity(twiml.toXML()).build();
    }

    @POST
    @Path("/send")
    public Response sendRecordingSms(@FormParam("RecordingUrl") String recordingUrl,
                                     @FormParam("From") String recipientNumber,
                                     @FormParam("DialCallDuration") int duration,
                                     @FormParam("DialCallStatus") String callStatus) throws TwiMLException {
        TwiMLResponse twiml = new TwiMLResponse();

        switch (callStatus) {
            case "no-answer":
                twiml.append(new Say("The recipient did not pick up."));
                break;
            case "busy":
                twiml.append(new Say("The recipient is busy. Please try again later."));
                twiml.append(new Hangup());
                break;
            case "failed":
                twiml.append(new Say("The call failed. Please check the phone number"));
                break;
            case "completed":
                boolean recipientHungUpDuringConfirmation = duration <= 10;
                if (recipientHungUpDuringConfirmation) {
                    twiml.append(new Say("The recipient declined the call."));
                } else {
                    try {
                        TwilioClient.getInstance().sendSms(recipientNumber, recordingUrl);
                    } catch (TwilioRestException e) {
                        twiml.append(new Say("An internal error has occurred."));
                    }
                }
                break;
        }
        return Response.ok().entity(twiml.toXML()).build();
    }

}