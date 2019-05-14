import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibleScholar {

    /**
     * <pre>
     * 根据kjv.txt和stopwords.txt，定位出现频率最高的12个单词及最低的12个单词
     * lord:7886
     * god:3823
     * ......
     * </pre>
     */

    private static HashMap<String, Integer> stopWordsHash;

    private static HashMap<String, Integer> wordsTable;

    private static BibleScholar bs = new BibleScholar();



    public String[] resolve() {


        Map<String, Integer> oneMap = bs.getOneNumMap(wordsTable);
        Map<String, Integer> wordMap = bs.sortByValue(wordsTable);

        String[] wordArr = new String[24];
        String curStr;
        Iterator<Map.Entry<String, Integer>> entries = wordMap.entrySet().iterator();
        int i = 0;
        while (entries.hasNext()) {

            Map.Entry<String, Integer> entry = entries.next();
            curStr = entry.getKey() + ":" + entry.getValue();
//            System.out.println(curStr);
            wordArr[i] = curStr;
            i++;
            if (i > 12) break;
        }

        Iterator<Map.Entry<String, Integer>> entries1 = oneMap.entrySet().iterator();
        while (entries.hasNext()) {

            Map.Entry<String, Integer> entry = entries1.next();
            curStr = entry.getKey() + ":" + entry.getValue();
//            System.out.println(curStr);
            wordArr[i] = curStr;
            i++;
            if (i > 23) break;
        }

        bs.showArray(wordArr);
        return null;
    }


    private <String extends Comparable<? super String>, Integer > Map<String, Integer> getOneNumMap(Map<String, Integer> map) {
        Map<String, Integer> result = new LinkedHashMap<>();
        Map<String, Integer> oneMap = new LinkedHashMap<>();

        Iterator<Map.Entry<String, Integer>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            if (entry.getValue().equals(1)) {
                oneMap.put(entry.getKey(), entry.getValue());
            }
        }


        oneMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByKey()
                        .reversed()).forEachOrdered(
                                e -> result.put(e.getKey(), e.getValue()));
//        System.out.println(result);
        return result;

    }

    private <String, Integer extends Comparable<? super Integer>> Map<String, Integer> sortByValue(Map<String, Integer> map) {
        Map<String, Integer> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
//        System.out.println(result);
        return result;

    }


    public static void main(String[] args) {
        stopWordsHash = getStopWordsData();

        getNumberFromkjv();

        bs.resolve();
    }



    private static void getNumberFromkjv() {
        java.lang.String kjvFilePath = BibleScholar.class.getClassLoader().getResource("kjv.txt").getPath();
        File file = new File(kjvFilePath);

        wordsTable = new HashMap<>();


        Integer curHashValue = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            java.lang.String st;
            while ((st = br.readLine()) != null) {
                Pattern p = Pattern.compile("[A-Za-z]+");
                Matcher m = p.matcher(st);
                while (m.find()) {
                    String findStr = m.group().toLowerCase();
                    if (!stopWordsHash.containsKey(findStr)) {
                        if (!wordsTable.containsKey(findStr)) {
                            wordsTable.put(findStr, 1);
                        } else {
                            curHashValue = wordsTable.get(findStr);
                            wordsTable.replace(findStr, ++curHashValue);
                        }


//                        System.out.println("cureentkey===" + findStr + "  value===" + curHashValue);
                    }
                }



            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static HashMap<String, Integer> getStopWordsData() {
        java.lang.String stopWordsFilePath = BibleScholar.class.getClassLoader().getResource("stopwords.txt").getPath();
        File file = new File(stopWordsFilePath);

        HashMap<String, Integer> map = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            java.lang.String st;
            while ((st = br.readLine()) != null) {
                map.put(st, 0);
            }
            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }
    }


    private void showArray(String[] arr) {
        for (String st :arr) {
            System.out.println(st);
        }
    }

}
