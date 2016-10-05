package ee.teoreteetik.callRecorder;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwilioClient {

    private final TwilioRestClient restClient;
    private final String voiceNumber;
    private final String smsNumber;

    public TwilioClient(String accountSid, String authToken, String voiceNumber, String smsNumber) {
        restClient = new TwilioRestClient(accountSid, authToken);
        this.voiceNumber = voiceNumber;
        this.smsNumber = smsNumber;
    }

    public void makeOutgoingCall(String recipientNumber, String callbackUrl) throws TwilioRestException {
        Account mainAccount = restClient.getAccount();
        CallFactory callFactory = mainAccount.getCallFactory();
        Map<String, String> callParams = new HashMap<>();
        callParams.put("To", recipientNumber);
        callParams.put("From", this.voiceNumber);
        callParams.put("Url", callbackUrl);
        callFactory.create(callParams);
    }

    public void sendSms(String recipientNumber, String msgBody) throws TwilioRestException {
        MessageFactory messageFactory = restClient.getAccount().getMessageFactory();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Body", msgBody));
        params.add(new BasicNameValuePair("To", recipientNumber));
        params.add(new BasicNameValuePair("From", this.smsNumber));
        messageFactory.create(params);
    }

}
