import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
// import com.dampcake.bencode.Bencode; - available if you need it!


public class Main {
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        String command = args[0];
        BencodeParser benParse = new BencodeParser();

        if("decode".equals(command)) {
            String bencodedValue = args[1];
            Object decoded;
            try {
                decoded = benParse.decodeBencode(bencodedValue);
            } catch(RuntimeException e) {
                System.out.println(e.getMessage());
                return;
            }
            System.out.println(gson.toJson(decoded));
        } else if("info".equals(command)) {
            String torrentFilePath = args[1];
            byte[] fileData = readTorrentFile(torrentFilePath);
            String bencodedValue = new String(fileData, StandardCharsets.ISO_8859_1);

            Object decoded = benParse.decodeBencode(bencodedValue);

            if(decoded instanceof Map){
                Map<String, Object> torrentData = (Map<String, Object>) decoded;

                String trackerURL = (String) torrentData.get("announce");
                System.out.println("Tracker URL: " + trackerURL);

//                System.out.println(torrentData);
                Map<String, Object> infoMap = (Map<String, Object>) torrentData.get("info");
                Long fileLength = (Long) infoMap.get("length");
                System.out.println("Length: " + fileLength);

                Re_encode encode = new Re_encode();
                String infoHash = encode.infoDictHasher(infoMap);
                System.out.println("Info Hash: " + infoHash);

                Long pieceLength = (Long) infoMap.get("piece length");
                System.out.println("Piece Length: " + pieceLength);

                encode.pieceHashes(infoMap.get("pieces"));
            } else
                System.out.println("Invalid torrent file format");
        } else if ("peers".equals(command)) {
            String torrentFilePath = args[1];
            byte[] fileData = readTorrentFile(torrentFilePath);
            String bencodedValue = new String(fileData, StandardCharsets.ISO_8859_1);
            Object decoded = benParse.decodeBencode(bencodedValue);

            if(decoded instanceof Map){
                Map<String, Object> torrentData = (Map<String, Object>) decoded;

                String trackerURL = (String) torrentData.get("announce");
//                System.out.println("Tracker URL: " + trackerURL);

                Map<String, Object> infoMap = (Map<String, Object>) torrentData.get("info");
                Long fileLength = (Long) infoMap.get("length");
//                System.out.println("Length: " + fileLength);

                Re_encode encode = new Re_encode();
                String infoHash = encode.infoDictHasher(infoMap);
//                System.out.println("Info Hash: " + infoHash);

                DiscoverPeers discoverPeers = new DiscoverPeers(trackerURL,infoHash, fileLength);
                discoverPeers.sendGetRequest();
            } else
                System.out.println("Invalid torrent file format");
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

    static byte[] readTorrentFile(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();
        return data;
    }

}
