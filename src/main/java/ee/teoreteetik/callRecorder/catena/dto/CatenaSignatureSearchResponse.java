package ee.teoreteetik.callRecorder.catena.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatenaSignatureSearchResponse {

    public final List<CatenaPersistedSignature> signatures;

    @JsonCreator
    public CatenaSignatureSearchResponse(@JsonProperty("content") List<CatenaPersistedSignature> signatures) {
        this.signatures = signatures;
    }
}
