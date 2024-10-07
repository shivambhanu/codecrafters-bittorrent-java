import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DiscoverPeers {
    private String infoHash;
    private String peerId = "00112233445566778899";
    private Integer port = 6881;
    private Long uploaded = (long) 0;
    private Long downloaded = (long) 0;
    private Long left;
    private Integer compact = 1;

    private String trackerUrl = "";
    public DiscoverPeers(String trackerUrl, String infoHash, Long left) {
        this.trackerUrl = trackerUrl;
        this.infoHash = infoHash;
        this.left = left;
    }


    public String hexToUrl(String hexInfoHash) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < hexInfoHash.length(); i+=2)
            sb.append('%').append(hexInfoHash.substring(i, i+2).toUpperCase());
        return sb.toString();
    }


    public void sendGetRequest() {
        String urlEncodedInfoHash = hexToUrl(infoHash);

        String fullUrl = String.format("%s?info_hash=%s&peer_id=%s&port=%d&uploaded=%d&downloaded=%d&left=%d&compact=%d", trackerUrl, urlEncodedInfoHash, peerId, port, uploaded, downloaded, left, compact);

        System.out.println(fullUrl);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .GET()
                .build();

        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            parseTrackerResponse(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void parseTrackerResponse(byte[] response) {
        String bencodedValue = new String(response, StandardCharsets.ISO_8859_1);
        BencodeParser benParse = new BencodeParser();
        Object decoded = benParse.decodeBencode(bencodedValue);

        // grab the peers field.
        Map<String, Object> torrentData = (Map<String, Object>) decoded;
        System.out.println(torrentData);
    }

}
