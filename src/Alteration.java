import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alteration {

    public static void main(String[] args) {

        String str = "the battle at london bridge is victorious, all personnel at london bridge are ordered to reposition to oxford street, 200 tanks at london bridge are ordered to reposition to big ben";

        System.out.println(Encryptor.encrypt(str));
        System.out.println(Decryptor.decrypt(Encryptor.encrypt(str)));
        System.out.println(str);
    }

    static boolean bruteForce(String str, ArrayList<int[]> arrayList) {
        String inter = "";
        for (int i = 0; i < arrayList.size(); i++) {
            inter = str;
            for (int j = 0; j < 6; j++) {
                inter = Encryptor.e(arrayList.get(i)[j], inter);
            }
            for (int j = 5; j >= 0; j--) {
                inter = Decryptor.d(arrayList.get(i)[j], inter);
            }
            if (inter.equals(str)) {
                System.out.println(Arrays.toString(arrayList.get(i)));
            }
        }

        return false;
    }

    public static void permutation(String str, ArrayList<int[]> arrayList) {
        permutation("", str, arrayList);
    }

    private static void permutation(String prefix, String str, ArrayList<int[]> arrayList) {
        int[] arr = new int[6];
        int n = str.length();
        if (n == 0) {
            for (int i = 0; i < prefix.split("").length; i++) {
                arr[i]=Integer.parseInt(prefix.split("")[i]);
            }
            arrayList.add(arr);
        }
        else
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), arrayList);
    }
}

