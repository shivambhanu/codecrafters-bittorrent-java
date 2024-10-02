import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
// import com.dampcake.bencode.Bencode; - available if you need it!

public class Main {
  private static final Gson gson = new Gson();

  public static void main(String[] args) throws Exception {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    // System.out.println("Logs from your program will appear here!");
    String command = args[0];
    if("decode".equals(command)) {
      //  Uncomment this block to pass the first stage
        String bencodedValue = args[1];
        Object decoded;
        try {
          decoded = decodeBencode(bencodedValue);
        } catch(RuntimeException e) {
          System.out.println(e.getMessage());
          return;
        }
        System.out.println(gson.toJson(decoded));

    } else {
      System.out.println("Unknown command: " + command);
    }

  }

  static Object decodeBencode(String bencodedString) {
    if (Character.isDigit(bencodedString.charAt(0))) {
      int firstColonIndex = 0;
      for(int i = 0; i < bencodedString.length(); i++) { 
        if(bencodedString.charAt(i) == ':') {
          firstColonIndex = i;
          break;
        }
      }
      int length = Integer.parseInt(bencodedString.substring(0, firstColonIndex));
      return bencodedString.substring(firstColonIndex+1, firstColonIndex+1+length);
    } else if(bencodedString.charAt(0) == 'i'){
      int len = bencodedString.length();
      return Long.parseLong(bencodedString.substring(1, len-1));
    } else if(bencodedString.charAt(0) == 'l') {
      String data = bencodedString.substring(1, bencodedString.length()-1);
      int idx = 0; // iterator to data string
      List<Object> resList = new ArrayList<>();

      while(idx < data.length()){
        if(Character.isDigit(data.charAt(idx))){
            int firstColonIndex = 0;
            for(int i = idx; i < data.length(); i++) {
                if(data.charAt(i) == ':') {
                    firstColonIndex = i;
                    break;
                }
            }
            int length = Integer.parseInt(data.substring(idx, firstColonIndex));
            resList.add(data.substring(firstColonIndex+1, firstColonIndex+1+length));
            idx = firstColonIndex + 1 + length;
        }else{
            idx++;
            StringBuilder sb = new StringBuilder();
            while(data.charAt(idx) != 'e'){
                sb.append(data.charAt(idx));
                idx++;
            }
            resList.add(Long.parseLong(sb.toString()));
            idx++;
        }
      }
      return resList;
    } else {
      throw new RuntimeException("Only strings are supported at the moment");
    }
  }
  
}
