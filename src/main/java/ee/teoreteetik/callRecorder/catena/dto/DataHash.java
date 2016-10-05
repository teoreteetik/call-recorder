package ee.teoreteetik.callRecorder.catena.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataHash {

    @JsonProperty("algorithm") public final String algorithm;
    @JsonProperty("value") public final String value;

    @JsonCreator
    public DataHash(@JsonProperty("algorithm") String algorithm, @JsonProperty("value") String value) {
        this.algorithm = algorithm;
        this.value = value;
    }
}
