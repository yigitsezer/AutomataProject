import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main2 {

    static ArrayList<String> terminals = new ArrayList<>();
    static ArrayList<String> nonTerminals = new ArrayList<>();
    static HashMap<String, ArrayList<String>> grammarMap = new HashMap<>();
    static HashMap<String, ArrayList<String>> equivalents = new HashMap<>();


    public static void main(String[] args) {
        String grammar = "<S0> -> <M1> <M2>|<S1> <F1>|<F5> <F3>|<F4> <F3>|<S2> <F1>|<F7> <F2>|<F6> <F2>|<F10> <F9>\n" +
                "<S1> -> <M1> <M2>|<S1> <F1>\n" +
                "<M1> -> <F11> <encounter-verb>\n" +
                "<M2> -> <prep> <time-or-place>\n" +
                "<S2> -> <F5> <F3>|<F4> <F3>|<S2> <F1>\n" +
                "<S3> -> <F7> <F2>|<F6> <F2>\n" +
                "<R0> -> <F10> <F9>\n" +
                "<F1> -> <comma> <R0>\n" +
                "<F2> -> <prep> <place>\n" +
                "<F3> -> <Verb1> <result>\n" +
                "<F4> -> <w1> <F2>\n" +
                "<F5> -> <Article> <F4>\n" +
                "<F6> -> <H1> <H2>\n" +
                "<F7> -> <Article> <F6>\n" +
                "<F8> -> <amount> <unit>\n" +
                "<F9> -> <H3> <G7>\n" +
                "<F10> -> <F8> <F2>\n" +
                "<F11> -> <side> <unit>\n" +
                "<G1> -> <agreement> <w2>\n" +
                "<G2> -> <compromise> <w3>\n" +
                "<G3> -> <nation> <w4>\n" +
                "<G4> -> <nation> <w5>\n" +
                "<G5> -> <Verb1> <order>\n" +
                "<G6> -> <w8> <movement>\n" +
                "<G7> -> <w8> <place>\n" +
                "<H1> -> <G1> <G2>\n" +
                "<H2> -> <G3> <G4>\n" +
                "<H3> -> <G5> <G6>\n" +
                "<w5> -> <J1> <w7>\n" +
                "<J1> -> <w6> <Article>\n" +
                "<comma> -> ,\n" +
                "<w1> -> battle\n" +
                "<w2> -> has been\n" +
                "<w3> -> between\n" +
                "<w4> -> and\n" +
                "<w6> -> for\n" +
                "<w7> -> engagement\n" +
                "<w8> -> to\n" +
                "<Verb1> -> is|are\n" +
                "<Article> -> a|the\n" +
                "<prep> -> in|on|at\n" +
                "<time-or-place> -> morning|noon|afternoon|night|midnight|london bridge|big ben|london eye|oxford street|eastern front|western front\n" +
                "<place> -> london bridge|big ben|london eye|oxford street|eastern front|western front\n" +
                "<side> -> enemy|elly|neutral\n" +
                "<unit> -> personnel|tanks|aircrafts|\n" +
                "<encounter-verb> -> spotted|seen|destroyed|eliminated\n" +
                "<time> -> morning|noon|afternoon|night|midnight\n" +
                "<front> -> eastern front|western front\n" +
                "<land> -> london bridge|big ben|london eye|oxford street\n" +
                "<result> -> victorious|lost|inconclusive\n" +
                "<amount> -> all|10|50|100|200|1000\n" +
                "<order> -> advised|ordered\n" +
                "<movement> -> retreat|move|go|deploy|reposition\n" +
                "<agreement> -> truce|parley|ceasefire\n" +
                "<compromise> -> agreed|signed|made\n" +
                "<nation> -> britain|germany|italy|france|spain";

        String s = "";


        Pattern p = Pattern.compile("<.+?>");
        Matcher m = p.matcher(grammar);
        while (m.find()) {
            s = m.group();
            if (!nonTerminals.contains(s))
                nonTerminals.add(m.group());
        }

        p = Pattern.compile("(?<=[ \\|])\\b[a-zA-Z0-9 ]+\\b(?=\\|)?");
        m = p.matcher(grammar);
        while (m.find()) {
            s = m.group();
            if (!terminals.contains(s))
                terminals.add(m.group());
        }

        for (String i : grammar.split("\n")) {
            String[] arr = i.split(" -> ");
            String[] tempTerminals = arr[1].trim().split("\\|");
            for (String j : tempTerminals)
                if (!grammarMap.containsKey(j)) {
                    ArrayList<String> arrList = new ArrayList<>();
                    arrList.add(arr[0]);
                    grammarMap.put(j, arrList);
                } else {
                    grammarMap.get(j).add(arr[0]);
                }
        }
        equivalents.putAll(grammarMap);

        String str = "the battle at london bridge is victorious, all personnel at london bridge are ordered to reposition to oxford street , 200 tanks at london bridge are ordered to reposition to big ben";
        System.out.println(cyk(str));
    }

    static boolean cyk(String str) {
        ArrayList<String> words = new ArrayList<>();
        Pattern p = Pattern.compile("(\\b[a-zA-Z0-9]+\\b|,)");
        Matcher m = p.matcher(str);
        while (m.find()) {
            words.add(m.group());
        }

        String[][] groups;
        String[][] split;
        String[] currentGroup;
        String[] combinations;

        for (int i = 0; i < words.size(); i++) {
            groups = getWordGroups(i+1, words.toArray(new String[0]));
            for (int j = 0; j < groups.length; j++) {
                currentGroup = groups[j];
                for (int l = 0; l < currentGroup.length - 1; l++) {
                    split = splitAt(l + 1, currentGroup);
                    if (equivalents.containsKey(merge(split[0])) && equivalents.containsKey(merge(split[1]))) {
                        combinations = getAllCombinations(equivalents.get(merge(split[0])).toArray(new String[0]), equivalents.get(merge(split[1])).toArray(new String[0]));
                        for (String n : combinations) {
                            if (isGrammaticallyValid(n)) {
                                if (!equivalents.containsKey(merge(currentGroup))) {
                                    ArrayList<String> temp = new ArrayList<>(equivalents.get(n));
                                    equivalents.put(merge(currentGroup), temp);
                                } else {
                                    for (String k:equivalents.get(n)) {
                                        if (!equivalents.get(merge(currentGroup)).contains(k)) {
                                            equivalents.get(merge(currentGroup)).add(k);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return equivalents.containsKey(merge(words.toArray(new String[0])));
    }

    static String merge(String[] words) {
        StringBuilder concat = new StringBuilder();
        for (String i : words)
            concat.append(i).append(" ");
        return concat.toString().trim();
    }

    //TODO: incomplete
    static boolean isGrammaticallyValid(String str) {
        if (grammarMap.containsKey(str) && !equivalents.containsKey(str)) {
            ArrayList<String> temp = new ArrayList<>(grammarMap.get(str));
            equivalents.put(str, temp);
            return true;
        } else if (equivalents.containsKey(str)) {
            for (String i : equivalents.get(str))
                if (isGrammaticallyValid(i))
                    if (!equivalents.containsKey(i))
                        for (String j:equivalents.get(i))
                            if (!equivalents.get(i).contains(j))
                                equivalents.get(i).add(j);
            return true;
        } else {
            return false;
        }
    }

    static String[][] getWordGroups(int groupLength, String[] words) {
        String[][] groups = new String[words.length - groupLength + 1][groupLength];
        int start = 0;
        for (int i = 0; i < groups.length; i++) {
            for (int j = 0; j < groupLength; j++) {
                groups[i][j] = words[j + start];
            }
            start++;
        }
        return groups;
    }

    static String[][] splitAt(int at, String[] words) {
        String[][] split = new String[2][];
        split[0] = new String[at];
        split[1] = new String[words.length - at];
        for (int i = 0; i < at; i++) {
            split[0][i] = words[i];
        }
        for (int i = at; i < words.length; i++) {
            split[1][i - at] = words[i];
        }
        return split;
    }

    static String[] getAllCombinations(String[] from, String[] to){
        int length = from.length * to.length;
        int counter = 0;
        String[] combinations = new String[length];
        if(length == 0){ return combinations; };
        for(int i = 0; i < from.length; i++){
            for(int j = 0; j < to.length; j++){
                combinations[counter] = from[i] + " " + to[j];
                counter++;
            }
        }
        return combinations;
    }

    static String[] concat(String[] a, String[] b) {
        String[] concat = new String[a.length + b.length];
        int endPoint = 0;
        for (int i = 0; i < a.length; i++) {
            concat[i] = a[i];
            endPoint = i;
        }
        for (int i = 0; i < b.length; i++) {
            concat[i + endPoint] = b[i];
        }
        return concat;
    }
}
