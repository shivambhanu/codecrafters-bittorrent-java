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
                Map<String, Object> torrentInfo = (Map<String, Object>) decoded;

                String trackerURL = (String) torrentInfo.get("announce");
                System.out.println("Tracker URL: " + trackerURL);

                Map<String, Object> infoMap = (Map<String, Object>) torrentInfo.get("info");
                Long fileLength = (Long) infoMap.get("length");
                System.out.println("Length: " + fileLength);

                Re_encode encode = new Re_encode();
                String encodedInfo = encode.encodeInfoDict(infoMap);
                System.out.println("Info Hash: " + encodedInfo);
            } else {
                System.out.println("Invalid torrent file format");
            }

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
