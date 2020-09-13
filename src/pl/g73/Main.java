package pl.g73;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static Scanner sc;
    static FileWriter writer = null;
    static StringBuffer sb = new StringBuffer();

    public static void main(final String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));  //nice
        List<String> argsList = new ArrayList<>();
        Arrays.asList(args).forEach(a -> argsList.add(a));


        // I/O File, console
        sc = new Scanner(System.in);
        if (argsList.contains("-inputFile")) {
            String inputFile = argsList.get(argsList.indexOf("-inputFile") + 1);
            argsList.remove(inputFile);
            sc = new Scanner(new File(inputFile));
        }
        if (argsList.contains("-outputFile")) {
            String outputFile = argsList.get(argsList.indexOf("-outputFile") + 1);
            argsList.remove(outputFile);
            writer = new FileWriter(new File(outputFile));
        }

        // checking command line arguments
        Set<String> correctArgs = Set.of("-sortingType", "natural", "byCount", "-dataType", "long", "line", "word", "-inputFile", "-outputFile");
        HashSet<String> argsSet = new HashSet<>(argsList);
        argsSet.removeAll(correctArgs);
        argsSet.forEach(a -> System.out.println("\"" + a + "\" isn't a valid parameter. It's skipped."));


        String sortingType = "natural";
        if (argsList.contains("-sortingType")) {
            String secArg = null;
            try {
                secArg = argsList.get(argsList.indexOf("-sortingType") + 1);
            } catch (Exception e) {
                System.out.println("No sorting type defined!");
                System.exit(0);
            }
            if (secArg.equals("byCount")) sortingType = "byCount";
        }

        if (argsList.contains("-dataType")) {
            String dataTypeArgument = null;
            try {
                dataTypeArgument = argsList.get(argsList.indexOf("-dataType") + 1);
            } catch (Exception e) {
                System.out.println("No data type defined!");
                System.exit(0);
            }
            switch (dataTypeArgument) {
                case "long":
                    longSeparator(sortingType);
                    break;
                case "line":
                    lineSeparator(sortingType);
                    break;
                default:
                    wordSeparator(sortingType);
            }
        }
        sc.close();
    }
//#######################################################################################################

    private static void wordSeparator(String sortingType) throws IOException {
        if (sortingType.equals("natural")) wordSeparatorNatural();
        if (sortingType.equals("byCount")) wordSeparatorByCount();
    }

    private static void wordSeparatorNatural() throws IOException {
        List<String> list = new ArrayList<>();
        int counter = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] splitedLineIntoWords = line.split("(\\s+)");
            for (String oneWord : splitedLineIntoWords) {
                counter++;
                list.add(oneWord);
            }
        }
        Collections.sort(list);

        sb.append(String.format("Total words: %d\n", counter));
        sb.append("Sorted data: \n");
        for (String word : list) {
            sb.append(word + " ");
        }
        saveFile(writer, sb);
    }

    private static void wordSeparatorByCount() throws IOException {
        HashMap<String, Integer> dataHashMap = new HashMap<>();
        int counter = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] splitetLineIntoWords = line.split("(\\s+)");
            for (String oneWord : splitetLineIntoWords) {
                counter++;
                dataHashMap.put(oneWord, dataHashMap.getOrDefault(oneWord, 0) + 1);
            }
        }

        TreeSet<Integer> setOfValues = new TreeSet<>(dataHashMap.values());

        Map<Integer, Set<String>> reversedAndSetOfKeys = new HashMap<>();
        for (Integer integer : setOfValues) {
            Set<String> v = new TreeSet<>();
            for (String key : dataHashMap.keySet()) {
                if (Objects.equals(dataHashMap.get(key), integer)) v.add(key);
            }
            reversedAndSetOfKeys.put(integer, v);
        }

        sb.append(String.format("Total words: %d.\n", counter));
        for (Integer integer : setOfValues) {
            for (String string : reversedAndSetOfKeys.get(integer)) {
                sb.append(String.format("%s: %d time(s), %d%%\n", string, integer, Math.round((100 * integer) / (float) counter)));
            }
        }
        saveFile(writer, sb);
    }

    //#######################################################################################################
    private static void lineSeparator(String sortingType) throws IOException {
        if (sortingType.equals("natural")) lineSeparatorNatural();
        if (sortingType.equals("byCount")) lineSeparatorByCount();
    }

    private static void lineSeparatorNatural() throws IOException {
        List<String> list = new ArrayList<>();
        int counter = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            counter++;
            list.add(line);
        }

        Collections.sort(list);

        sb.append(String.format("Total lines: %d\n", counter));
        sb.append("Sorted data: \n");
        for (String line : list) {
            sb.append(line + " ");
        }
        saveFile(writer, sb);
    }

    private static void lineSeparatorByCount() throws IOException {
        HashMap<String, Integer> dataHashMap = new HashMap<>();
        int counter = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            counter++;
            dataHashMap.put(line, dataHashMap.getOrDefault(line, 0) + 1);
        }

        TreeSet<Integer> setOfValues = new TreeSet<>(dataHashMap.values());

        Map<Integer, Set<String>> reversedAndSetOfKeys = new HashMap<>();
        for (Integer integer : setOfValues) {
            Set<String> v = new TreeSet<>();
            for (String key : dataHashMap.keySet()) {
                if (Objects.equals(dataHashMap.get(key), integer)) v.add(key);
            }
            reversedAndSetOfKeys.put(integer, v);
        }

        sb.append(String.format("Total numbers: %d.\n", counter));
        for (Integer integer : setOfValues) {
            for (String string : reversedAndSetOfKeys.get(integer)) {
                sb.append(String.format("%s: %d time(s), %d%%\n", string, integer, Math.round((100 * integer) / (float) counter)));
            }
        }
        saveFile(writer, sb);
    }

    //#######################################################################################################
    private static void longSeparator(String sortingType) throws IOException {
        if (sortingType.equals("natural")) longSeparatorNatural();
        if (sortingType.equals("byCount")) longSeparatorByCount();
    }

    private static void longSeparatorNatural() throws IOException {
        List<Long> longList = new ArrayList<>();

        int counter = 0;
        long l;
        while (sc.hasNextLine()) {
            String[] split = sc.nextLine().split("\\s+");
            for (String s : split) {
                try {
                    l = Long.parseLong(s);
                    counter++;
                    longList.add(l);
                } catch (NumberFormatException e) {
                    System.out.println("\"" + s + "\" isn't a long. It's skipped.");
                }
            }
        }
        Collections.sort(longList);

        sb.append(String.format("Total numbers: %d\n", counter));
        sb.append("Sorted data: ");
        for (Long aLong : longList) {
            sb.append(aLong + " ");
        }
        saveFile(writer, sb);
    }

    private static void longSeparatorByCount() throws IOException {
        TreeMap<Long, Integer> dataLong = new TreeMap<>();

        int counter = 0;
        long l;
        while (sc.hasNextLine()) {
            String[] split = sc.nextLine().split("\\s+");
            for (String s : split) {
                try {
                    l = Long.parseLong(s);
                    counter++;
                    dataLong.put(l, dataLong.getOrDefault(l, 0) + 1);
                } catch (NumberFormatException e) {
                    System.out.println("\"" + s + "\" isn't a long. It's skipped.");
                }
            }
        }

        TreeSet<Integer> setOfValues = new TreeSet<>(dataLong.values());

        Map<Integer, Set<Long>> reversedAndSetOfKeys = new HashMap<>();
        for (Integer integer : setOfValues) {
            Set<Long> v = new LinkedHashSet<>();
            for (Long keyLong : dataLong.keySet()) {
                if (Objects.equals(dataLong.get(keyLong), integer)) v.add(keyLong);
            }
            reversedAndSetOfKeys.put(integer, v);
        }

        sb.append(String.format("Total numbers: %d.\n", counter));
        for (Integer integer : setOfValues) {
            for (Long aLong : reversedAndSetOfKeys.get(integer)) {
                sb.append(String.format("%d: %d time(s), %d%%\n", aLong, integer, Math.round((100 * integer) / (float) counter)));
            }
        }
        saveFile(writer, sb);
    }

    //#######################################################################################################
    public static void saveFile(FileWriter w, StringBuffer s) throws IOException {
        if (w != null) {
            w.write(s.toString());
            System.out.println("File saved");
            w.close();
        } else {
            System.out.println(s.toString());
        }
    }
}