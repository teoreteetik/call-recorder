package ee.teoreteetik.call_recorder;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class TwilioClient {

    private static final TwilioClient INSTANCE = new TwilioClient(Config.ACCOUNT_SID, Config.AUTH_TOKEN);

    public static TwilioClient getInstance() {
        return INSTANCE;
    }

    private final TwilioRestClient restClient;

    private TwilioClient(String accountSid, String authToken) {
        restClient = new TwilioRestClient(accountSid, authToken);
    }

    public void makeOutgoingCall(String recipientNumber, String callbackUrl) throws TwilioRestException {
        Account mainAccount = restClient.getAccount();
        CallFactory callFactory = mainAccount.getCallFactory();
        Map<String, String> callParams = new HashMap<>();
        callParams.put("To", recipientNumber);
        callParams.put("From", Config.VOICE_NUMBER);
        callParams.put("Url", callbackUrl);
        callFactory.create(callParams);
    }

    public void sendSms(String recipientNumber, String msgBody) throws TwilioRestException {
        MessageFactory messageFactory = restClient.getAccount().getMessageFactory();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Body", msgBody));
        params.add(new BasicNameValuePair("To", recipientNumber));
        params.add(new BasicNameValuePair("From", Config.SMS_NUMBER));
        messageFactory.create(params);
    }

}