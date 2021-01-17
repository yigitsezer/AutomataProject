import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decryptor {

    static String decrypt(String str) {
        return decrypt1(decrypt2(decrypt3(decrypt4(decrypt5(decrypt6(str))))));
        //return decrypt1(decrypt6(decrypt2(decrypt4(decrypt5(decrypt3(str))))));
    }

    static String d(int code, String str) {
        if (code == 1) {
            return decrypt1(str);
        } else if (code == 2) {
            return decrypt2(str);
        } else if (code == 3) {
            return decrypt3(str);
        } else if (code == 4) {
            return decrypt4(str);
        } else if (code == 5) {
            return decrypt5(str);
        } else if (code == 6) {
            return decrypt6(str);
        } else
            return "";
    }

    private static String decrypt1(String str) {
        Pattern p = Pattern.compile("\\b([a-zA-Z0-9])([a-zA-Z0-9]*?)([a-zA-Z0-9])\\b");
        Matcher m = p.matcher(str);
        return m.replaceAll("$3$2$1").toLowerCase();
    }

    private static String decrypt2(String str) {
        Pattern p = Pattern.compile("(\\b(in|on|at)\\b)");
        Matcher m = p.matcher(str);
        int c = 0;
        while(m.find()){
            c += 1;
        }
        String str1 = "";
        p = Pattern.compile("(.*) (\\b(in|on|at)\\b) (.*)");
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

    private static String decrypt3(String str) {
        Pattern p = Pattern.compile("\\b(in|on|at)\\b ([A-Za-z0-9]*)");
        Matcher m = p.matcher(str);
        return m.replaceAll("$2 $1");
    }

    private static String decrypt4(String str) {
        Pattern p = Pattern.compile("y\\$c");
        Matcher m = p.matcher(str);
        return m.replaceAll(" ");
    }

    private static String decrypt5(String str) {
        Pattern p = Pattern.compile("([aeiouAEIOU])([aeiouAEIOU])\\2\\1");
        Matcher m = p.matcher(str);
        return m.replaceAll("$2$1");
    }

    private static String decrypt6(String str) {
        Pattern p = Pattern.compile("([0-9a-zA-z]*)([aeiouAEIOU])( )([aeiouAEIOU])([0-9a-zA-z]*)");
        Matcher m = p.matcher(str);
        return m.replaceAll("$5$2$3$4$1");
    }
}
