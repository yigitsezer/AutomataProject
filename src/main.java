import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class main {
	static ArrayList<String> terminals = new ArrayList<>();
    static ArrayList<String> nonTerminals = new ArrayList<>();
    static HashMap<String, ArrayList<String>> grammarMap = new HashMap<>();
    static HashMap<String, ArrayList<String>> equivalents = new HashMap<>();
    static HashMap<String, ArrayList<String>> productionRules = new HashMap<>();
    
    
    public static void main(String[] args) {
    	
    	String str12 = "I saw the man with the telescope";
        String str2 = "enemy personnel spotted in morning";
        String str3 = "the battle at london bridge is victorious, all personnel at london bridge are ordered to reposition to oxford street, 200 tanks at london bridge are ordered to reposition to big ben, 200 tanks at london bridge are ordered to reposition to big ben";
        
        String grammar = readGrammar("example-grammar3.txt");
        buildGrammar(grammar);
        /*
        System.out.println(cyk(str12));
        System.out.println(str12.toLowerCase());
        System.out.println(equivalents.get(merge(getWords(str12).toArray(new String[0]))));  */ 
        getValidSentences(grammar,5);
        
        getInvalidSentences(5);
 
    }   
   
    private static void getValidSentences(String grammar, int numberOfSentences) {
    	for (String i : grammar.split("\n")) {
            String[] arr = i.split(" -> ");
            if(productionRules.containsKey(arr[0]))
            	productionRules.get(arr[0]).addAll(Arrays.asList(arr[1].trim().split("\\|")));
            else	
            	productionRules.put(arr[0], new ArrayList<>(Arrays.asList(arr[1].trim().split("\\|"))));
        }        
        try{
            FileWriter fw = new FileWriter("example-sentences.txt"); //the true will append the new data
            for (int i = 0; i < numberOfSentences; i++) 
            	fw.write(getBottom(nonTerminals.get(0)).replaceAll(" +", " ") +"\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe) {
        	System.err.println("IOException: " + ioe.getMessage());
        }
    }
    
    private static void getInvalidSentences(int numberOfSentences) {
    	Random random = new Random();
    	try{
			List<String> lines = Files.readAllLines(Paths.get("example-sentences.txt"), StandardCharsets.UTF_8);
			for (int i = 0; i < numberOfSentences; i++) {
	    		int numberOfWords =random.ints(terminals.size()/4, terminals.size()).findFirst().getAsInt();
	    		StringBuilder stringBuilder = new StringBuilder();
	    		for (int j = 0; j < numberOfWords ; j++) {
					stringBuilder.append(terminals.get(random.nextInt(terminals.size()))).append(" ");
				}
	    		lines.add(random.nextInt(lines.size()), stringBuilder.toString().trim());
			}
			Files.write(Paths.get("input.txt"), lines, StandardCharsets.UTF_8);
		}
		catch(IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
    }
    
    
    
    private static String getBottom(String start) {
        Random random = new Random();
        if (start.contains("<")) {
        	StringBuilder stringBuilder = new StringBuilder();
            ArrayList<String> temp = productionRules.get(start);
            String production = temp.get(random.nextInt(temp.size()));
            for (String i : production.replaceAll("><","> <").split(" "))
                stringBuilder.append(getBottom(i)).append(" ");
            return stringBuilder.toString();
        } else
            return start;
    }
    
    
    private static String readGrammar(String grammar) {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(grammar), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
    
    private static void buildGrammar(String grammar) {    	
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
                } else 
                    grammarMap.get(j).add(arr[0]);
        }
    }

    private static boolean cyk(String str) {
        ArrayList<String> words = getWords(str);
        String[][] groups;
        String[][] split;
        String[] currentGroup;
        
        for (int i = 0; i < words.size(); i++) {
            groups = getWordGroups(i+1, words.toArray(new String[0]));
            for (int j = 0; j < groups.length; j++) {
                currentGroup = groups[j]; // [0]-> baa [1] -> aab [2] -> aba if string=baaba
                ArrayList<String> temp = new ArrayList<String>();
                if(groups[j].length >1) {
                	for (int j2 = 1; j2 < currentGroup.length; j2++) {
                		ArrayList<String>[] cartesianParts = new ArrayList[2];
                		split = splitAt(j2,groups[j]);
                		
                		for (int j3 = 0; j3 < 2; j3++)
                			isGrammaticallyValid(split[j3]);
                		
                		cartesianParts[0] = new ArrayList<String>();
                		cartesianParts[1] = new ArrayList<String>();
                		for (int k = 0; k < 2; k++) {
                			String str1 = merge(split[k]);
							if(equivalents.containsKey(str1) || grammarMap.containsKey(str1)) 
									cartesianParts[k].addAll(equivalents.get(str1));
                		}
                		
                		//Cartesian Product
            			String[] temp1 = cartesianParts[0].toArray(new String[cartesianParts[0].size()]);
                		String[] temp2 = cartesianParts[1].toArray(new String[cartesianParts[1].size()]);
                		String[] cartesianProducts = getAllCombinations(temp1,temp2);
                		
                		for (int k = 0; k < cartesianProducts.length; k++) { 
                			if(grammarMap.containsKey(cartesianProducts[k]) && !temp.contains(cartesianProducts[k])) { 
            					for (String string : grammarMap.get(cartesianProducts[k]))
            						if(!temp.contains(string))
            							temp.add(string);
                				equivalents.put(merge(currentGroup), temp);
                			}
                		}
                	}
                }
                else
                	isGrammaticallyValid(groups[j]);
            }
        }
        return equivalents.containsKey(merge(words.toArray(new String[0])));
    }

   
    
    private static boolean isGrammaticallyValid(String[] words) {
        return isGrammaticallyValid(merge(words));
    }
    
    private static String merge(String[] words) {
        StringBuilder concat = new StringBuilder();
        for (String i : words)
            concat.append(i).append(" ");
        return concat.toString().trim();
    }

    private static boolean isGrammaticallyValid(String str) {
        ArrayList<String> temp;
        if (grammarMap.containsKey(str) && !equivalents.containsKey(str)) {
            temp = new ArrayList<>();
            for (String i : grammarMap.get(str))
                temp.add(i);
            equivalents.put(str, temp);
            return true;
        } else if (equivalents.containsKey(str))
            return true;
        else
            return false;
    }

    private static ArrayList<String> getWords(String str) {
    	ArrayList<String> words = new ArrayList<>();
        Pattern p = Pattern.compile("(\\b[a-zA-Z0-9]+\\b|[,.:;%?])");
        Matcher m = p.matcher(str);
        while (m.find()) {
            words.add(m.group());
        }    
        return words;
    }
    
    private static String[][] getWordGroups(int groupLength, String[] words) {
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

    private static String[][] splitAt(int at, String[] words) {
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

    private static String[] getAllCombinations(String[] from, String[] to){
        int length = from.length * to.length;
        int counter = 0;
        String[] combinations = new String[length];
        if(length == 0){ return combinations; };
        for(int i = 0; i < from.length; i++){
            for(int j = 0; j < to.length; j++){
                combinations[counter++] = from[i] + to[j];
            }
        }
        return combinations;
    }
}