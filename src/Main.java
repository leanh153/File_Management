/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LeVanAnh
 */
public class Main {

    //contains a list of MyFile
    private static MyFile[] files;

    //ctor
    public Main() {
        files = null;
    }

    // scanner
    private static final Scanner scan = new Scanner(System.in);

    //get information of all text files under given folder name
    private static void loadFiles(String folder) {
        List<MyFile> listFiles = new ArrayList<>();
        loadFiles(folder, listFiles);
        files = listFiles.stream().toArray(MyFile[]::new);
    }

    private static void loadFiles(String folder, List<MyFile> listFiles) {
        /*insert the code for listing all text files under given folder here*/

        File[] listFileFolder;
        File file = null;
        try {
            file = new File(folder);
            if (!file.isDirectory() || !file.exists()) {
                System.out.println("It's not folder or not exist");
                return;
            }
            listFileFolder = file.listFiles();

            for (File f : listFileFolder) {
                if (f.isFile()) {
                    MyFile myfile = new MyFile();
                    myfile.setFullPath(f.getAbsolutePath());
                    myfile.setSize(f.length());
                    myfile.setName(f.getName());
                    listFiles.add(myfile);

                } else if (f.isDirectory()) {
                    loadFiles(f.getPath(), listFiles);
                }
            }

        } catch (Exception e) {
            System.out.println("Problem when load files");
        }
    }

    //list information of all loaded files
    private static void list(MyFile[] files) {
        if (files != null && files.length > 0) {
            //output heading
            System.out.println(String.format("%-20s%-10s", "Name", "Size(in byte)"));
            for (MyFile f : files) {
                System.out.println(f);
            }
        } else {
            System.out.println("List of files is empty...");
        }
    }

    //sort the list of files ascending (use selection sort)
    private static void selectionSort(SortField sw) {
        /*You should insert code for sorting here, you are going to sort the
        list of loaded files named "files" ascending by ascending order.*/
        if (files.length > 0 || files != null) {
            int len = files.length;
            boolean isSortBySize = (sw == SortField.SIZE);

            for (int i = 0; i < len - 1; i++) {
                int min_index = i;
                for (int j = i + 1; j < len; j++) {
                    if (isSortBySize
                            ? files[j].getSize() < files[min_index].getSize()
                            : stringCompare(files[j].getName().toLowerCase(), files[min_index].getName().toLowerCase()) < 0) {
                        min_index = j;
                    }

                }
                MyFile temp = files[min_index];
                files[min_index] = files[i];
                files[i] = temp;
            }

        }

    }

    //sort the list of files ascending  (use insertion sort)
    private static void insertionSort(SortField sw) {
        //do nothing if list of files is empty
        /*You should insert code for sorting here, you are going to sort the list of
        loaded files named "files" ascending by ascending order.*/
        if (files.length > 0 || files != null) {
            boolean isSortBySize = (sw == SortField.SIZE);
            int len = files.length;
            for (int i = 1; i < len; i++) {
                MyFile key = files[i];
                int j = i - 1;
                while (j >= 0 && (isSortBySize
                        ? files[j].getSize() > key.getSize()
                        : stringCompare(files[j].getName().toLowerCase(), key.getName().toLowerCase()) > 0)) {
                    files[j + 1] = files[j];
                    j--;
                }
                files[j + 1] = key;
            }

        }
    }

    /**
     * This function takes last element as pivot, places the pivot element at
     * its correct position in sorted array, and places all smaller (smaller
     * than pivot) to left of pivot and all greater elements to right of pivot
     */
    private static int partition(boolean isSortBySize, MyFile[] files,
            int low, int high) {

        MyFile pivot = files[high];
        int i = (low - 1);// index of smaller element
        for (int j = low; j < high; j++) {
            // if isSortBySize is true then "files[j].getSize() < pivot.getSize()"
            // occur else compare and sort by name case insensitive
            if (isSortBySize
                    ? files[j].getSize() < pivot.getSize()
                    : stringCompare(files[j].getName().toLowerCase(),
                            pivot.getName().toLowerCase()) < 0) {
                i++;

                // swap files[i] and files[j]
                MyFile temp = files[i];
                files[i] = files[j];
                files[j] = temp;
            }

        }

        // swap files[i] and files[j]
        MyFile temp = files[i + 1];
        files[i + 1] = files[high];
        files[high] = temp;
        return i + 1;
    }

    /**
     * sort the list of files ascending (use quick sort algorithm) low -->
     * starting index, high --> ending index
     */
    private static void quickSort(boolean isSortBySize, MyFile[] files,
            int low, int high) {
        if (low < high) {
            // pi is patitioning index
            int pi = partition(isSortBySize, files, low, high);
            quickSort(isSortBySize, files, low, pi - 1);
            quickSort(isSortBySize, files, pi + 1, high);
        }

    }

//sort and output sorted list of text files
    private static void sort(SortType st, SortField sw) {
        switch (st) {
            case INSERTIONSORT:
                insertionSort(sw);
                break;
            case SELECTIONSORT:
                selectionSort(sw);
                break;
            case QUICKSORT:
                quickSort(sw == SortField.SIZE, files, 0, files.length - 1);
                break;
            default:
                break;
        }
        //output result after sorting
        list(files);
    }

    // get text from file
    private static String getTextFromFile(String absolutePath) {
        FileReader fr = null;
        LineNumberReader lnr = null;
        String lineString;
        String text = "";

        try {

            fr = new FileReader(absolutePath);
            lnr = new LineNumberReader(fr);
            // get all text in file
            while ((lineString = lnr.readLine()) != null) {
                text += lineString + "\n";
            }

        } catch (IOException e) {
            System.out.println("Searching error");
        } catch (Exception e) {
            System.out.println("Searching error");
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (lnr != null) {
                    lnr.close();
                }
            } catch (Exception e) {
                System.out.println("Close read file error!");
            }

        }

        return text;
    }

    //return true if given MyFile contains given keyword, otherwise return false
    public static boolean searchFile(MyFile mf, String keyword) {
        if (!mf.getName().toLowerCase().endsWith(".txt")) {
            return false;
        }
        //read the content of mf and see if keyword is in the content of mf or not
        /*You can use LineNumberReader to read the content of given mf and check out if
        the content of given mf contains keyword. This function should return true if 
        the searching is found, otherwise return false*/

        String text = getTextFromFile(mf.getFullPath());
        String patternString = ".*" + keyword + ".*";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();

    }

    //output information of all files which content has given keyword
    private static void searchFile(String keyword) {
        //save all files which matched given keyword to the list and output the list
        List<MyFile> listFiles = new ArrayList<>();
        for (MyFile f : files) {
            if (searchFile(f, keyword)) {
                listFiles.add(f);
            }
        }
        MyFile[] foundFiles = listFiles.stream().toArray(MyFile[]::new);
        list(foundFiles);
    }

    /** this gets nextInt from input,
        msg is choices
        startRange is starting choice number
        endRange is ending number*/
    private static int intReader(String msg, int startRange, int endRange) {
        int choice = 0;
        boolean loop = true;
        while (loop) {

            try {
                System.out.print(msg);
                choice = scan.nextInt();
                if (choice >= startRange && choice <= endRange) {
                    loop = false;
                } else {
                    System.out.println("Check your number");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input!");
                scan.next();
            }

        }
        return choice;
    }

    // get full path from file name
    private static String getFullPath(String fileName) {
        String fullPath = "";
        for (MyFile f : files) {
            if (fileName.equalsIgnoreCase(f.getName())) {
                return fullPath = f.getFullPath();
            }
        }
        return fullPath;
    }

    // show content of the file
    private static void showContent(String fileName) {
        String fullPath = getFullPath(fileName);
        String textFromFile = "";
        if (fullPath.length() > 0) {
            textFromFile = getTextFromFile(fullPath);

        } else {
            System.out.println("File isn't found");
        }

        if (textFromFile.length() > 0) {
            System.out.println("====>File start<====\n" + textFromFile + "====>File end<====");

        }
    }

    /**
     * compare 2 string return 0 > 2 string equal return negative string 1 < string 2
     * return positive then string1 > string 2
     */
    private static int stringCompare(String str1, String str2) {
        // loop through each char in string and compare them
        for (int i = 0; i < str1.length() && i < str2.length(); i++) {
            if (str1.charAt(i) == str2.charAt(i)) {
                // if 2 char has the same value in ascii continue the loop
                continue;
            } else {
                // else return this integer
                return str1.charAt(i) - str2.charAt(i);
            }
        }

        if (str1.length() == str2.length()) {
            // both string are equal
            return 0;
        } else if (str1.length() < str2.length()) {
            return str1.length() - str2.length();
        } else {
            return str2.length() - str1.length();
        }
    }

    public static void main(String[] args) {
        boolean keepWoring = true;
        while (keepWoring) {
            switch (intReader("Menu\n1.Load files\n2.Sort files\n3.Search files"
                    + "\n4.Show file content\n0.Exit\nEnter your choice: ", 0, 4)) {
                case 1:
                    System.out.print("Enter a folder: ");
                    loadFiles(scan.next());
                    list(files);
                    break;
                case 2:
                    // if files null or length equal 0 then need to load folder first
                    if (files == null || files.length == 0) {
                        System.out.print("Folder not loaded yet, Enter a folder: ");
                        loadFiles(scan.next());
                        list(files);
                    }
                    int sortBy;
                    switch (intReader("Sort the list of files by using"
                            + "\n1.Selection sort\n2.Insertion sort"
                            + "\n3.Quick sort\nYour choice: ", 1, 3)) {
                        case 1:
                            sortBy = intReader("====>Sort by<===\n1.Sort by name"
                                    + "\n2.Sort by size\nYour choice: ", 1, 2);
                            sort(SortType.SELECTIONSORT, sortBy == 1
                                    ? SortField.NAME : SortField.SIZE);
                            break;
                        case 2:
                            sortBy = intReader("====>Sort by<===\n1.Sort by name"
                                    + "\n2.Sort by size\nYour choice: ", 1, 2);
                            sort(SortType.INSERTIONSORT, sortBy == 1
                                    ? SortField.NAME : SortField.SIZE);
                            break;
                        case 3:
                            sortBy = intReader("====>Sort by<===\n1.Sort by name"
                                    + "\n2.Sort by size\nYour choice: ", 1, 2);
                            sort(SortType.QUICKSORT, sortBy == 1
                                    ? SortField.NAME : SortField.SIZE);
                            break;
                    }

                    break;
                case 3:
                    // if files null or length equal 0 then need to load folder first
                    if (files == null || files.length == 0) {
                        System.out.print("Folder not loaded yet, Enter a folder: ");
                        loadFiles(scan.next());
                        list(files);
                    }
                    System.out.print("Enter any keyword to search: ");
                    searchFile(scan.next());
                    break;
                case 4:
                    // if files null or length equal 0 then need to load folder first
                    if (files == null || files.length == 0) {
                        System.out.print("Folder not loaded yet, Enter a folder: ");
                        loadFiles(scan.next());
                        list(files);
                    }
                    System.out.print("Enter file's name to show: ");
                    showContent(scan.next());

                    break;
                case 0:
                    System.out.println("Exit program");
                    keepWoring = false;
                    break;
            }
        }

    }
}
