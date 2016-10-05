package ee.teoreteetik.callRecorder.catena;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CatenaSignature {

    public final LocalDateTime aggregationTime;

    CatenaSignature(long aggregationTimeInEpochMillis){
        long epochSeconds = aggregationTimeInEpochMillis / 1000;
        this.aggregationTime = LocalDateTime.ofEpochSecond(epochSeconds, 0, ZoneOffset.UTC);
    }
}
