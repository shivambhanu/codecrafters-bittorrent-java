import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Re_encode {

    public String encodeInfoDict(Map<String, Object> infoDict) throws Exception {
        // encode the data in lexicographical order(used treemap for this already while decoding)
        StringBuilder bencode = new StringBuilder();
        bencode.append('d');

        for(String key : infoDict.keySet()){
            bencode.append(key.length()).append(":").append(key);
            Object value = infoDict.get(key);

            if(value instanceof Long) {
                bencode.append('i').append(value).append('e');
            } else if(value instanceof String) {
                String strVal = (String) value;
                bencode.append(strVal.length()).append(':').append(strVal);
            } else if(value instanceof byte[]){
                byte[] byteArray = (byte[]) value;
                bencode.append(byteArray.length).append(':');
                bencode.append(new String(byteArray, "ISO-8859-1"));  // Preserve the
            }else{
                System.out.println("Invalid data type in dictionary!");
            }
        }
        bencode.append('e');

       String sha1Hash = calculateSHA1(bencode.toString().getBytes("ISO-8859-1"));
       return sha1Hash;
    }


    public String calculateSHA1(byte[] data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(data);

            StringBuilder hexString = new StringBuilder();
            for(byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch( NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "error while hashing!";
    }
}