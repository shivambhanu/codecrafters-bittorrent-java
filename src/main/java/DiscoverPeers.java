import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String hexToUrl(String hexInfoHash) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < hexInfoHash.length(); i+=2) {
            sb.append('%').append(hexInfoHash.substring(i, i+2).toUpperCase());
        }

//        System.out.println("Url encoded hash: " + sb.toString());
        return sb.toString();
    }


//    public byte[] hexToByteArray(String hexInfoHash) {
//        int len = hexInfoHash.length();
//        byte[] byteArr = new byte[len/2];
//
//        for(int i = 0; i < len; i+=2){
//            byteArr[i/2] = (byte) ((Character.digit(hexInfoHash.charAt(i), 16) << 4) + Character.digit(hexInfoHash.charAt(i+1), 16));
//        }
//
//        return byteArr;
//    }
}
