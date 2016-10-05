package ee.teoreteetik.callRecorder;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

import io.dropwizard.Configuration;

public class Config extends Configuration {

    @JsonProperty("twilio")
    @Valid
    public TwilioConfig twilioConfig;
    @JsonProperty("catena")
    @Valid
    public CatenaConfig catenaConfig;

    public class TwilioConfig{
        @NotEmpty @Valid public String accountSid;
        @NotEmpty @Valid public String authToken;
        @NotEmpty @Valid public String voiceNumber;
        @NotEmpty @Valid public String smsNumber;
    }

    public class CatenaConfig {
        @NotEmpty @Valid public String user;
        @NotEmpty @Valid public String password;
        @NotEmpty @Valid public String baseUrl;
    }
}
