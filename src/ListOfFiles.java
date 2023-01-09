import com.sun.jdi.ArrayReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ListOfFiles {
    private static String extension = ".txt";

    /**
     * Функция рекурсивно обходит все папки и файлы корневой директории
     *         и пополняет наш словарь типа <название файла, его зависимости>
     * @param dirPath - абсолютный путь до корневой папки (объект типа File)
     * @param arr - словарь зависимостей, тип словаря: <название файла, строка с require>
     * @param folder_path - абсолютный путь до корневой папки (просто строка)
     * @param all_files - массив ВСЕХ файлов в корневой папке и ее подпапках
     */
    public static void listOfFiles(File dirPath, Map<String, ArrayList<String>> arr, String folder_path, ArrayList<String> all_files){
        File filesList[] = dirPath.listFiles();
        for (File file : filesList) {
            if (file.isFile()) {
//                System.out.println("File path: " + file.getName());
                try {
                    Scanner myReader = new Scanner(file);
                    String relative_name = file.getPath().replaceAll(folder_path, "");
                    while (myReader.hasNextLine()) {
                        String line = myReader.nextLine();

                        if (line.contains("require")) {  // есть ли в этой строчке зависимости
//                            System.out.println("line: " + line);
                            line = line.trim().
                                    replaceAll("require", "").
                                    replaceAll("‘", "").
                                    replaceAll("’", "XYZ").
                                    trim();  // extract нужное из строки
//                            System.out.println("line: " + line);
                            String[] requires = line.split("XYZ");  // XYZ - разделитель

                            // зависимости из текущего файла
                            ArrayList<String> dependencies = new ArrayList<String>();

                            // добавляем зависимые файлы в наш массив
                            for (int i = 0; i < requires.length; ++i) {
                                dependencies.add(requires[i].trim() + extension);
                            }

                            // сохраняем имя файла и его зависимости
                            ArrayList<String> temp_arr = arr.get(relative_name);
                            if (temp_arr == null) {
                                arr.put(relative_name, dependencies);
                            } else {
                                for (int i = 0; i < dependencies.size(); ++i) {
                                    temp_arr.add(dependencies.get(i));
                                }
                                arr.put(relative_name, temp_arr);
                            }

                            System.out.println("RN: " + relative_name + " | DEP: " + dependencies);
//                            System.out.println("////////////////////////");
//                            System.out.println(file.getPath().replaceAll(folder_path, ""));
//                            System.out.println("////////////////////////");
                        }
                    }
                    myReader.close();
                    all_files.add(relative_name);
                } catch (Exception e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

            } else if (file.isDirectory()) {
                listOfFiles(file, arr, folder_path, all_files);
            }
        }
    }

    /**
     * Функция выводит на экран весь словарь типа <название файла; все его зависимости>
     * @param arr - словарь типа <название файла; все его зависимости>
     */
    private static void showDebugInfo(Map<String, ArrayList<String>> arr) {
        System.out.println();
        System.out.println("---------- Our map: ----------");
        for(Map.Entry<String,  ArrayList<String>> item : arr.entrySet()){
            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
        }
        System.out.println("----------  ----------");
        System.out.println("\n");
    }

    /**
     * Функция печатает все содержимое файла с путем full_path
     * @param full_path - абсолютный путь до корневой папки (просто строка)
     */
    private static void printFileContent(String full_path) {
        System.out.println("Чтение файла НАЧАТО");
        try {
            File file = new File(full_path + extension);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                //process the line
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("Чтение файла ЗАКОНЧЕНО");
    }

    public static void main(String args[]) throws IOException {
        // Creating a File object for directory
        String folder_path = "/Users/egorfortov/Desktop/KPO/";  // корневая директория
        File file = new File(folder_path);  // передаем путь к нужной папке с файлами (т.е. к корневой директории)

        Map<String, ArrayList<String>> states = new HashMap<String, ArrayList<String>>();  // <название файла, строка с require>

        ArrayList<String> all_files = new ArrayList<>();
        // Ищем все файлы в нашей корневой директории
        listOfFiles(file, states, folder_path, all_files);


        // вывод всего нашего map-a, то есть всех файлов, содержащих зависимости и их самих
        showDebugInfo(states);

        System.out.println("\n\n\n");

        ArrayList<String> separate_files = new ArrayList<String>();
        for (int i = 0; i < all_files.size(); ++i) {
            if (!states.containsKey(all_files.get(i))) {
                separate_files.add(all_files.get(i));
            }
        }

        for (int i = 0; i < separate_files.size(); ++i) {
            System.out.println(all_files.get(i));
        }

        System.out.println("\n\n\n");


//        System.out.println("\n--------------------------");
//        System.out.println(((states.get("File 2-2.txt")).toArray())[0] + ".txt");
//        printFileContent(folder_path + ((states.get("File 2-2.txt")).toArray())[0]);


        // WORKING --- BEGIN
        // делаем словарь вида <название файла, его уник.номер>
        Map<String, Integer> filesAndIndexes = new HashMap<String, Integer>();
        int fileIndex = 0;  // уникальный индекс файла
        for(Map.Entry<String, ArrayList<String>> item : states.entrySet()){
            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
            for (String elem: item.getValue()) {
//                System.out.println("index: " + fileIndex);
                if (!filesAndIndexes.containsKey(elem)) {
                    filesAndIndexes.put(elem, fileIndex);
                    System.out.println("elem: " + elem);
//                    System.out.println("index: " + filesAndIndexes.get(elem));
                    ++fileIndex;
                }
            }
            String elem = item.getKey();
            if (!filesAndIndexes.containsKey(elem)) {
                filesAndIndexes.put(elem, fileIndex);
//                System.out.println("elem: " + elem);
//                    System.out.println("index: " + filesAndIndexes.get(elem));
                ++fileIndex;
            }
        }

        System.out.println("///////////////////////////");  // for DEBUG only
        for(Map.Entry<String,  Integer> item : filesAndIndexes.entrySet()){
            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
        }
        System.out.println("///////////////////////////");  // for DEBUG only
        // WORKING --- END


        // словарь типа <индекс файла, индексы файлов от которых он зависит>
        Map<Integer, ArrayList<Integer>> mapForTopologySort = new HashMap<Integer, ArrayList<Integer>>();
        for(Map.Entry<String, ArrayList<String>> item : states.entrySet()){
            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
            ArrayList<Integer> indexes = new ArrayList<Integer>();  // массив "правых" индексов
            String value = item.getKey();
//            System.out.println(value);
            int file_index = filesAndIndexes.get(value);
            for (int i = 0; i < item.getValue().size(); ++i) {
                String str = item.getValue().get(i);
                int inner_file_index = filesAndIndexes.get(str);
                indexes.add(inner_file_index);
            }
            mapForTopologySort.put(file_index, indexes);
        }

        System.out.println("Зависимости файлах в индексах:");
        // теперь можем работать со словарем mapForTopologySort, используем его
        // для топологической сортировки и проверки на цикличность нашего графа
        for(Map.Entry<Integer,  ArrayList<Integer>> item : mapForTopologySort.entrySet()) {
            System.out.print(item.getKey() + ": ");
            for (int i = 0; i < item.getValue().size(); ++i) {
                int index2 = item.getValue().get(i);
                System.out.print(index2 + " ");
            }
            System.out.println();
        }


        ////////////////////////////// KEY ALGORITHM //////////////////////////////
        int V = filesAndIndexes.size();  // количество вершин в графе = количество файлов
        Graph g = new Graph(V);

        // вводим однонаправленные ребра графа - НАЧАЛО
        for(Map.Entry<Integer,  ArrayList<Integer>> item : mapForTopologySort.entrySet()) {
            for (int i = 0; i < item.getValue().size(); ++i) {
                int index2 = item.getValue().get(i);
                g.addEdge(item.getKey(), index2);
            }
        }
//        g.addEdge(1, 2);
//        g.addEdge(2, 3);
//        g.addEdge(3, 4);
//        g.addEdge(3, 5);
//        g.addEdge(4, 2);
        // вводим однонаправленные ребра графа - КОНЕЦ

        if (g.isCyclic()) {
            System.out.println("\nГраф цикличен, невозможно выдать требуемый порядок файлов!");
            return;
        }
        // graph is Acyclic => make topology sort
        System.out.println("Топологическая сортировка графа:");
        Vector vec = new Vector();  // нужный нам порядок
        g.topologicalSort(vec);  // нужный нам порядок

        for (int i = 0; i < vec.size(); ++i) {
            System.out.print(vec.get(i) + " ");
        }

        ArrayList<String> all_printed_files = new ArrayList<>();

        // соотносим индексы их файлам  // NOT WORKING ?????
        System.out.println("\n#############################################");
        System.out.println("Итоговый порядок файлов:");
        int kolvo_outputs = 0;
        for (int i = 0; i < vec.size(); ++i) {
            final int j = i;
//            System.out.print(vec.get(i) + " ");
            filesAndIndexes.forEach((key, value) -> {
                if (value.equals(vec.get(j))) {
                    all_printed_files.add(key);
                    System.out.println(key);
                }
            });
        }

//        System.out.println("PRINTED FILES QUANTITY: " + all_printed_files.size());
        all_files.removeAll(all_printed_files);
//        System.out.println("ALL FOUND FILES QUANTITY: " + all_files.size());
        // вывод всех файлов, которые ни от кого не зависят и их нет
        // в зависимостях всех выведенных файлов
        for (int i = 0; i < all_files.size(); ++i) {
            System.out.println(all_printed_files.get(i));
            all_printed_files.add(all_files.get(i));
        }

        // теперь в all_printed_files лежат все файлы в нужном нам порядке
        System.out.println("CONCATENATED:");
        for (int i = 0; i < all_printed_files.size(); ++i) {
            // пропускаем скрытые файлы, сгенерированные mac os
            if (all_printed_files.get(i).contains("DS_Store")) {
                continue;
            }
            Path filePath = Path.of(folder_path + all_printed_files.get(i));
            String content = "CAN NOT READ THIS FILE \n";
            try {
                content = Files.readString(filePath);
            } catch (Exception e){};  // ошибку никак не обрабатываем, продолжаем читать файлы дальше

            System.out.println(content);
        }

    }
}