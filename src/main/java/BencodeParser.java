import java.util.*;

public class BencodeParser {
    private static int index;  // will be used across recursive calls
    private boolean rawByteFlag = false;

    public BencodeParser() {
        index = 0;
    }

    public Object decodeBencode(String bencodedString) {
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


    public Object parseString(String bencodedString) {
        int firstColonIndex = index;
        for(int i = index; i < bencodedString.length(); i++) {
            if(bencodedString.charAt(i) == ':') {
                firstColonIndex = i;
                break;
            }
        }
        int length = Integer.parseInt(bencodedString.substring(index, firstColonIndex));

        if(rawByteFlag){
            try {
                byte[] rawBytes = bencodedString.substring(firstColonIndex + 1, firstColonIndex + 1 + length).getBytes("ISO-8859-1");
                index = firstColonIndex + 1 + length;
                return rawBytes;
            } catch (Exception e){
                e.printStackTrace();
            }
        }else {
            String strVal = bencodedString.substring(firstColonIndex + 1, firstColonIndex + 1 + length);
            index = firstColonIndex + 1 + length;
            return strVal;
        }

        return "---error---";
    }


    public Object parseInteger(String bencodedString) {
        index++;
        int first_e = bencodedString.indexOf('e', index);
        Long longVal = Long.parseLong(bencodedString.substring(index, first_e));
        index = first_e + 1;
        return longVal;
    }


    public Object parseList(String bencodedString) {
        List<Object> list = new ArrayList<>();
        index++;  // skip the l of the list
        while(bencodedString.charAt(index) != 'e'){
            list.add(decodeBencode(bencodedString));
        }
        index++;  // skip the e of the list
        return list;
    }


    public Object parseDictionary(String bencodedString) {
        index++;  // skip d character
        Map<String, Object> map = new TreeMap<>();
        while(bencodedString.charAt(index) != 'e'){
            //parse key first
            String key = (String) decodeBencode(bencodedString);

            if(key.equals("pieces")) rawByteFlag = true;
            Object value = decodeBencode(bencodedString);
            if(key.equals("pieces")) rawByteFlag = false;

            map.put(key, value);
        }
        index++;  // skip e character
        return map;
    }

}
