import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import com.dampcake.bencode.Bencode; - available if you need it!

public class Main {
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        String command = args[0];
        if("decode".equals(command)) {
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


    // =============================  Bencode Parser ================================
    private static int index =  0;  // will be used across recursive calls

    static Object decodeBencode(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(index))) {
            return parseString(bencodedString);
        } else if(bencodedString.charAt(index) == 'i'){
            return parseInteger(bencodedString);
        } else if(bencodedString.charAt(index) == 'l') {
            return parseList(bencodedString);
        } else if(bencodedString.charAt(index) == 'd') {
            return parseDictionary(bencodedString);
        } else {
            throw new RuntimeException("Invalid bencode format detected!");
        }
    }


    static Object parseString(String bencodedString) {
        int firstColonIndex = index;
        for(int i = index; i < bencodedString.length(); i++) {
            if(bencodedString.charAt(i) == ':') {
                firstColonIndex = i;
                break;
            }
        }
        int length = Integer.parseInt(bencodedString.substring(index, firstColonIndex));
        String strVal = bencodedString.substring(firstColonIndex+1, firstColonIndex+1+length);
        index = firstColonIndex + 1 + length;

        return strVal;
    }


    static Object parseInteger(String bencodedString) {
        index++;
        int first_e = bencodedString.indexOf('e', index);
        Long longVal = Long.parseLong(bencodedString.substring(index, first_e));
        index = first_e + 1;
        return longVal;
    }


    static Object parseList(String bencodedString) {
        List<Object> list = new ArrayList<>();
        index++;  // skip the l of the list
        while(bencodedString.charAt(index) != 'e'){
            list.add(decodeBencode(bencodedString));
        }
        index++;  // skip the e of the list
        return list;
    }

    static Object parseDictionary(String bencodedString) {
        index++;  // skip d character
        Map<String, Object> map = new HashMap<>();
        while(bencodedString.charAt(index) != 'e'){
            //parse key first
            String key = (String) decodeBencode(bencodedString);
            //parse corresponding value
            Object value = decodeBencode(bencodedString);

            map.put(key, value);
        }
        index++;  // skip e character
        return map;
    }
  
}
