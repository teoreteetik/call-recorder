package ee.teoreteetik.callRecorder.catena.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class CatenaRequestBody {

    @JsonProperty("dataHash") public final DataHash dataHash;
    @JsonProperty("metadata") public final Map<String, Object> metadata;

    public CatenaRequestBody(DataHash dataHash, Map<String, Object> metadata) {
        this.dataHash = dataHash;
        this.metadata = metadata;
    }
}
