package ee.teoreteetik.callRecorder.catena.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatenaPersistedSignature {

    public final String id;
    public final SignatureDetails details;

    @JsonCreator
    public CatenaPersistedSignature(
            @JsonProperty("id") String id,
            @JsonProperty("details") SignatureDetails details) {

        this.id = id;
        this.details = details;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SignatureDetails{

        public final long aggregationTime;
        public final DataHash dataHash;

        @JsonCreator
        public SignatureDetails(
                @JsonProperty("aggregationTime") long aggregationTime,
                @JsonProperty("dataHash") DataHash dataHash) {

            this.aggregationTime = aggregationTime;
            this.dataHash = dataHash;
        }
    }
}
