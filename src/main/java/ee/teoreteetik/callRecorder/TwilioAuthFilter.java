package ee.teoreteetik.callRecorder;

import com.twilio.sdk.TwilioUtils;

import org.glassfish.jersey.server.ContainerRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import static java.util.stream.Collectors.toMap;

@Provider
public class TwilioAuthFilter implements ContainerRequestFilter {

    private final TwilioUtils twilioUtils;

    public TwilioAuthFilter(String authToken) {
        this.twilioUtils = new TwilioUtils(authToken);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        ContainerRequest cr = (ContainerRequest) requestContext;

        Map<String, String> formParams = getFormParams(cr);
        String url = cr.getAbsolutePath().toString();
        String xTwilioSig = requestContext.getHeaderString("x-twilio-signature");

        boolean isValid = twilioUtils.validateRequest(xTwilioSig, url, formParams);
        if (!isValid) {
            throw new RuntimeException("Not authorized");
        }
    }

    private Map<String, String> getFormParams(ContainerRequest cr) {
        cr.bufferEntity();
        MultivaluedMap<String, String> formParams = cr.readEntity(Form.class).asMap();
        return formParams.keySet().stream()
            .collect(toMap(Function.identity(),
                           formParams::getFirst,
                           (a, b) -> a,
                            HashMap::new));
    }

}
