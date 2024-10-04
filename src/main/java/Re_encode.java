import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Re_encode {

    public String encodeInfoDict(Map<String, Object> infoDict) {
        // encode the data in lexicographical order(used treemap for this already while decoding)
        StringBuilder sb = new StringBuilder();
        for(String key : infoDict.keySet()){
            sb.append(key.length()).append(":").append(key);

//            System.out.println(sb.toString());
            Object value = infoDict.get(key);
//            System.out.println(value.getClass());

            if(value instanceof Long) {
                sb.append('i').append(value).append('e');
            } else {
                String strVal = (String) value;
                if(key.equals("pieces")){
                    // Handling binary string explicitly, as it creates error if handled as normal string.
                    byte[] byteArray = strVal.getBytes(StandardCharsets.ISO_8859_1);

                    for(byte b : byteArray){
//                        System.out.print(b + ", ");
                        sb.append(b);
                    }

                }else{
                    sb.append(strVal.length()).append(':').append(strVal);
                }
            }
        }

//        System.out.println(" ---->  " + sb.toString())
        return calculateSHA1(sb.toString());
    }


    public String calculateSHA1(String input) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            byte[] hashBytes = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for(byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return ("SHA-1 hash: " + hexString.toString());
        } catch( NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "error while hashing!";
    }
}
