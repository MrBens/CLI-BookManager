package Utils;

import Entities.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class FileService {

    public static void writeInFile(String file, String inputString, boolean append){

        try(FileWriter fw = new FileWriter(file, append);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(inputString);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public static boolean isEntityExistsInFile(String file, Integer entityId) throws IOException {
        ArrayList<String> strings = readFile(file, true);


        for (String string : strings) {
            String[] columns = string.split(",");

            if (Integer.parseInt(columns[0]) == entityId){
                return true;
            }
        }
        return false;
    }

    public static void deleteFromFile(String file, Integer entityId) throws IOException {
        ArrayList<String> strings = readFile(file, true);
        clearFile(file);
        String stringToRemove = "";

        for (String string: strings) {
            String[] columns = string.split(",");

            if(Integer.parseInt(columns[0]) == entityId){
                stringToRemove = string;
            }
        }

        strings.remove(stringToRemove);

        for (String string : strings){
            writeInFile(file, string, true);
        }
    }

    public static void clearFile(String file) throws IOException {
        ArrayList<String> strings = readFile(file,false);
        String string = strings.get(0);
        writeInFile(file, string,false);
    }

    public static void updateStockFile(Integer entityId, boolean increment) throws IOException {
        String file = "db/Stock.csv";

        ArrayList<String> strings = readFile(file, true);
        clearFile(file);
        for (String string: strings) {
            String[] columns = string.split(",");

            if(Integer.parseInt(columns[0]) == entityId){
                int newQuantity = increment ? (Integer.parseInt(columns[1]) +1) : (Integer.parseInt(columns[1])-1);
                string = String.format("%s,%d", columns[0], newQuantity);
            }

            writeInFile(file, string, true);
        }
    }

    public static void updateStockFile(Integer entityId, Integer quantity) throws IOException {
        String file = "db/Stock.csv";

        ArrayList<String> strings = readFile(file, true);
        clearFile(file);
        for (String string: strings) {
            String[] columns = string.split(",");

            if(Integer.parseInt(columns[0]) == entityId){
                int newQuantity = quantity;
                string = String.format("%s,%d", columns[0], newQuantity);
            }

            writeInFile(file, string, true);
        }
    }

    public static void updateBookFile(Book book) throws IOException {
        String file = "db/Book.csv";
        ArrayList<String> strings = readFile(file, true);
        clearFile(file);
        for (String string: strings) {
            String[] columns = string.split(",");

            if(Integer.parseInt(columns[0]) == book.getId()){
                string = String.format("%d,%s,%s,%d,%s", book.getId(), book.getTitle(),book.getBookType(), book.getAuthor().getId(), book.getSynopsis());
            }

            writeInFile(file, string, true);
        }
    }

    public static void updateLoanFile(Loan loan) throws IOException {
        String file = "db/Loan.csv";
        ArrayList<String> strings = readFile(file, true);
        clearFile(file);
        for (String string: strings) {
            String[] columns = string.split(",");

            if(Integer.parseInt(columns[0]) == loan.getId()){
                string = String.format("%d,%d,%d,%d", loan.getId(), loan.getBorrower().getId(), loan.getBorrowedBook().getId(), loan.getStatus());
            }

            writeInFile(file, string, true);
        }
    }

    public static ArrayList<String> readFile(String file, boolean popHeader) throws IOException {

        ArrayList<String> strings = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line != null){
            strings.add(line);
            line = br.readLine();
        }

        if (popHeader){
            strings.remove(0);
        }
        return strings;
    }

    public static ArrayList<String> readBookFile() throws IOException {

        ArrayList<String> strings = readFile("db/Book.csv", true);
        ArrayList<String> outList = new ArrayList<>();

        if (strings.size() > 1){
            for (String string: strings) {
                String[] columns = string.split(",");
                Author author = EntityService.fetchAuthor(Integer.parseInt(columns[3]));
                String authorName = String.format("%s %s", author.getFirstName(), author.getLastName());
                String outString = String.format("ID : %d, TITLE : %s, TYPE : %s, AUTHOR : %s, SYNOPSYS : %s", Integer.parseInt(columns[0]),columns[1],columns[2],authorName,columns[4]);
                outList.add(outString);
            }
        }


        return outList;
    }

    public static ArrayList<String> readStockFile() throws IOException {

        ArrayList<String> strings = readFile("db/Stock.csv", true);
        ArrayList<String> outList = new ArrayList<>();

        for (String string: strings) {
            String[] columns = string.split(",");
            Book book = EntityService.fetchBook(Integer.parseInt(columns[0]));
            String outString = String.format("ID : %d, TITLE : %s, QUANTITY : %d", book.getId(),book.getTitle(), Integer.parseInt(columns[1]));
            outList.add(outString);
        }

        return outList;
    }

    public static String[] searchUserInFile(String file, String[] searchString) throws IOException {

        ArrayList<String> users = readFile(file, true);

        for (String user: users) {
            String[] columns = user.split(",");
            if (Objects.equals(columns[3], searchString[0]) && Objects.equals(columns[4], searchString[1])){
                return columns;
            }
        }

        return null;
    }

    public static boolean searchBookInFile(String file, Integer searchID) {

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null){

                String[] columns = line.split(",");
                if (Objects.equals(Integer.parseInt(columns[0]), searchID)){
                    return true;
                }
                line = br.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public static Integer getLastIndexOfFile(String file) throws IOException {
        return readFile(file, true).size()+1;
    }
}
