package ee.teoreteetik.callRecorder.catena;

import com.google.common.collect.ImmutableMap;

import java.io.InputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import ee.teoreteetik.callRecorder.catena.dto.CatenaPersistedSignature;
import ee.teoreteetik.callRecorder.catena.dto.CatenaRequestBody;
import ee.teoreteetik.callRecorder.catena.dto.CatenaSignatureSearchResponse;
import ee.teoreteetik.callRecorder.catena.dto.DataHash;

public class CatenaClient {
    private static final String FILE_URL_METADATA_KEY = "fileUrl";
    private static final String HASH_ALGORITHM = "SHA-256";

    private final Client httpClient;
    private final String catenaBaseUrl;
    private final Base64.Encoder encoder;

    public CatenaClient(Client httpClient, String catenaBaseUrl) {
        this.httpClient = httpClient;
        this.catenaBaseUrl = catenaBaseUrl;
        this.encoder = Base64.getEncoder();
    }

    public CatenaSignature signFile(String fileUrl) {
        String hashBase64 = this.getHashInBase64(fileUrl);

        CatenaRequestBody requestBody = new CatenaRequestBody(
                new DataHash(HASH_ALGORITHM, hashBase64),
                ImmutableMap.of(FILE_URL_METADATA_KEY, fileUrl));

        CatenaPersistedSignature catenaPersistedSignature = httpClient
                .target(catenaBaseUrl + "/api/v1/signatures")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .build("POST", Entity.json(requestBody))
                .invoke(CatenaPersistedSignature.class);

        return new CatenaSignature(catenaPersistedSignature.details.aggregationTime);
    }

    public Optional<CatenaSignature> getSignature(String fileUrl) {

        CatenaSignatureSearchResponse response = httpClient.target(catenaBaseUrl + "/api/v1/signatures")
                .queryParam("metadata." + FILE_URL_METADATA_KEY, fileUrl)
                .queryParam("sort", "createdAt,desc")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .build("GET")
                .invoke(CatenaSignatureSearchResponse.class);

        return response.signatures.stream()
                .findFirst()
                .map(s -> new CatenaSignature(s.details.aggregationTime));
    }

    private String getHashInBase64(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            InputStream is = url.openStream();
            DigestInputStream dis = new DigestInputStream(is, MessageDigest.getInstance(HASH_ALGORITHM));
            return encoder.encodeToString(dis.getMessageDigest().digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
