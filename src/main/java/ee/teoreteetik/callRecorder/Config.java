package ee.teoreteetik.callRecorder;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

import io.dropwizard.Configuration;

public class Config extends Configuration {

    @NotEmpty @Valid public String accountSid;
    @NotEmpty @Valid public String authToken;
    @NotEmpty @Valid public String voiceNumber;
    @NotEmpty @Valid public String smsNumber;

}
