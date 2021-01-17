import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Encryptor {

    static String encrypt(String str) {
        return encrypt6(encrypt5(encrypt4(encrypt3(encrypt2(encrypt1(str))))));
        //return encrypt3(encrypt5(encrypt4(encrypt2(encrypt6(encrypt1(str))))));
    }

    static String e(int code, String str) {
        if (code == 1) {
            return encrypt1(str);
        } else if (code == 2) {
            return encrypt2(str);
        } else if (code == 3) {
            return encrypt3(str);
        } else if (code == 4) {
            return encrypt4(str);
        } else if (code == 5) {
            return encrypt5(str);
        } else if (code == 6) {
            return encrypt6(str);
        } else
            return "";
    }

    private static String encrypt1(String str) {
        Pattern p = Pattern.compile("\\b([a-zA-Z0-9])([a-zA-Z0-9]*?)([a-zA-Z0-9])\\b");
        Matcher m = p.matcher(str);
        return m.replaceAll("$3$2$1").toLowerCase();
    }

    private static String encrypt2(String str) {
        Pattern p = Pattern.compile("(\\b(in|on|at)\\b)");
        Matcher m = p.matcher(str);
        int c = 0;
        while(m.find()){
            c += 1;
        }
        String str1 = "";
        p = Pattern.compile("(.*?) (\\b(in|on|at)\\b) (.*)");
        m = p.matcher(str);
        for (int i = 0; i < c; i++) {
            str1 = m.replaceAll("$4 $3 $1");
            m = p.matcher(str1);
        }

        if (str1.isEmpty())
            return str;
        else
            return str1;
    }

    private static String encrypt3(String str) {
        Pattern p = Pattern.compile("([A-Za-z0-9]*) (in|on|at)");
        Matcher m = p.matcher(str);
        return m.replaceAll("$2 $1");
    }

    private static String encrypt4(String str) {
        Pattern p = Pattern.compile("(?=.) (?=.)");
        Matcher m = p.matcher(str);
        return m.replaceAll("y\\$c");
    }

    private static String encrypt5(String str) {
        Pattern p = Pattern.compile("([aeiouAEIOU])([aeiouAEIOU])");
        Matcher m = p.matcher(str);
        return m.replaceAll("$2$1$1$2");
    }

    private static String encrypt6(String str) {
        Pattern p = Pattern.compile("([0-9a-zA-z]*)([aeiouAEIOU])( )([aeiouAEIOU])([0-9a-zA-z]*)");
        Matcher m = p.matcher(str);
        return m.replaceAll("$5$2$3$4$1");
    }
}
