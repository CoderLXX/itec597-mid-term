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


        // 获取逆序的Map数据
        Map<String, Integer> oneMap = bs.getOneNumMap(wordsTable);
        Map<String, Integer> wordMap = bs.sortByValue(wordsTable);

        // 格式化数据
        String[] wordArr = new String[24];
        String curStr;
        Iterator<Map.Entry<String, Integer>> entries = wordMap.entrySet().iterator();
        int i = 0;
        while (entries.hasNext()) {

            Map.Entry<String, Integer> entry = entries.next();
            curStr = entry.getKey() + ":" + entry.getValue();
            wordArr[i] = curStr;
            i++;
            if (i > 12) break;
        }

        Iterator<Map.Entry<String, Integer>> entries1 = oneMap.entrySet().iterator();
        while (entries.hasNext()) {

            Map.Entry<String, Integer> entry = entries1.next();
            curStr = entry.getKey() + ":" + entry.getValue();
            wordArr[i] = curStr;
            i++;
            if (i > 23) break;
        }

        return wordArr;
    }


    public static void main(String[] args) {
        // 读取文件内容
        bs.getStopWordsData();
        bs.getMapDataFromkjvFile();

        // 生成结果数组
        String[] result = bs.resolve();

        // 打印数组
        bs.showArray(result);
    }


    // 针对value低频的12个排序
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
        return result;

    }


    // 对整体的数据进行逆序排序
    private <String, Integer extends Comparable<? super Integer>> Map<String, Integer> sortByValue(Map<String, Integer> map) {
        Map<String, Integer> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;

    }



    // 读取kjv文件并保存数据在hashmap中
    private void getMapDataFromkjvFile() {
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
                    }
                }



            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // 从文件获取过滤词的hashmap数据
    private void getStopWordsData() {
        java.lang.String stopWordsFilePath = BibleScholar.class.getClassLoader().getResource("stopwords.txt").getPath();
        File file = new File(stopWordsFilePath);

        stopWordsHash = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            java.lang.String st;
            while ((st = br.readLine()) != null) {
                stopWordsHash.put(st, 0);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showArray(String[] arr) {
        for (String st :arr) {
            System.out.println(st);
        }
    }


}
