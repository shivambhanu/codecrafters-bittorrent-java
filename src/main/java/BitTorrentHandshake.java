import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Map;

public class BitTorrentHandshake {
    final private byte[] infoHash;
    final private byte[] peerId;


    public BitTorrentHandshake(Map<String, Object> infoMap) throws Exception {
        this.infoHash = generateInfoHash(infoMap);
        peerId = generatePeerId();
    }

    public void performHandshake() throws IOException {
        byte[] handshakeMessage = createHandshake(infoHash, peerId);

        //TODO: Parse ip and port dynamically instead of hardcoding it.
        Socket socket = new Socket("161.35.46.221", 51414);
        OutputStream out = socket.getOutputStream();
        out.write(handshakeMessage);
        out.flush();

        InputStream in = socket.getInputStream();
        byte[] response = new byte[68];
        int bytesRead = in.read(response);

        if(bytesRead == 68) {
            System.out.println("Handshake successful!");
            StringBuilder hexString = new StringBuilder();
            for(int i = 48; i < 68; i++) {
                byte b = response[i];
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            System.out.println(hexString);
        } else {
            System.out.println("Invalid response or connection error");
        }

        socket.close();
    }


    public static byte[] createHandshake(byte[] infoHash, byte[] peerId) {
        byte[] pstr = "BitTorrent protocol".getBytes();
        byte pstrlen = (byte) pstr.length;

        byte[] reserved = new byte[8];  // 8 reserved byte (set to 0)

        ByteBuffer buffer = ByteBuffer.allocate(49 + pstrlen);
        buffer.put(pstrlen);
        buffer.put(pstr);
        buffer.put(reserved);
        buffer.put(infoHash);
        buffer.put(peerId);

        return buffer.array();
    }


    public static byte[] generatePeerId() {
        return ("-BT0001-" + (System.currentTimeMillis() % 100000000000L)).getBytes();
    }


    public byte[] generateInfoHash(Map<String, Object> infoMap) throws Exception {
        // encode the data in lexicographical order(used treemap for this already while decoding)
        StringBuilder bencode = new StringBuilder();
        bencode.append('d');

        for(String key : infoMap.keySet()){
            bencode.append(key.length()).append(":").append(key);
            Object value = infoMap.get(key);

            if(value instanceof Long) {
                bencode.append('i').append(value).append('e');
            } else if(value instanceof String) {
                String strVal = (String) value;
                bencode.append(strVal.length()).append(':').append(strVal);
            } else if(value instanceof byte[]){
                byte[] byteArray = (byte[]) value;
                bencode.append(byteArray.length).append(':');
                bencode.append(new String(byteArray, "ISO-8859-1"));
            }else{
                System.out.println("Invalid data type in dictionary!");
            }
        }
        bencode.append('e');

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = sha1.digest(bencode.toString().getBytes("ISO-8859-1"));

        return hashBytes;
    }
}


