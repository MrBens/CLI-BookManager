package Utils;

import Entities.*;
import Enums.EnumBookType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static Utils.FileService.isEntityExistsInFile;
import static Utils.FileService.searchUserInFile;

public class EntityService {

    public static Author fetchAuthor(int authorId) throws IOException {

        Author authorInstance = new Author();
        ArrayList<String> authors = FileService.readFile("db/Author.csv",true);

        for (String author: authors) {
            String[] columns = author.split(",");
            if (Integer.parseInt(columns[0]) == authorId){
                authorInstance.setId(Integer.parseInt(columns[0]));
                authorInstance.setFirstName(columns[1]);
                authorInstance.setLastName(columns[2]);
            }
        }

        return authorInstance;
    }

    public static User fetchUser(int userId) throws IOException {

        User userInstance = new User();
        ArrayList<String> users = FileService.readFile("db/User.csv", true);

        for (String user: users) {
            String[] columns = user.split(",");
            if (Integer.parseInt(columns[0]) == userId){
                userInstance.setId(Integer.parseInt(columns[0]));
                userInstance.setFirstName(columns[1]);
                userInstance.setLastName(columns[2]);
                userInstance.setLogin(columns[3]);
                userInstance.setPassword(columns[4]);
            }
        }

        return userInstance;
    }

    public static User fetchUser(String[] userInfos) throws IOException {
        String[] userStrings = searchUserInFile("db/User.csv", userInfos);
        User user = new User();
        user.setId(Integer.parseInt(userStrings[0]));
        user.setFirstName(userStrings[1]);
        user.setLastName(userStrings[2]);
        user.setLogin(userStrings[3]);
        user.setPassword(userStrings[4]);
        user.setLoans(fetchLoanOfUser(user));

        return user;
    }

    public static Book fetchBook(int bookId) throws IOException {

        Book bookInstance = new Book();
        ArrayList<String> books = FileService.readFile("db/Book.csv",true);

        for (String book: books) {
            String[] columns = book.split(",");
            if (Integer.parseInt(columns[0]) == bookId){
                bookInstance.setId(Integer.parseInt(columns[0]));
                bookInstance.setTitle(columns[1]);
                bookInstance.setBookType(EnumBookType.valueOf(columns[2]));
                bookInstance.setAuthor(fetchAuthor(Integer.parseInt(columns[3])));
                bookInstance.setSynopsis(columns[4]);
            }
        }

        return bookInstance;
    }

    public static Loan fetchLoan(int loanId) throws IOException {

        Loan loanInstance = new Loan();
        ArrayList<String> loans = FileService.readFile("db/Loan.csv",true);

        for (String loan: loans) {
            String[] columns = loan.split(",");
            if (Integer.parseInt(columns[0]) == loanId){
                loanInstance.setId(Integer.parseInt(columns[0]));
                loanInstance.setBorrower(fetchUser(Integer.parseInt(columns[1])));
                loanInstance.setBorrowedBook(fetchBook(Integer.parseInt(columns[2])));
                loanInstance.setStatus(Integer.parseInt(columns[3]));
            }
        }

        return loanInstance;
    }

    public static ArrayList<Loan> fetchLoanOfUser(User user) throws IOException {

        ArrayList<Loan> outList = new ArrayList<>();
        ArrayList<String> loans = FileService.readFile("db/Loan.csv",true);

        if (loans.size() > 1){
            for (String loan: loans) {
                String[] columns = loan.split(",");
                if (Integer.parseInt(columns[1]) == user.getId()){
                    outList.add(fetchLoan(Integer.parseInt(columns[0])));
                }
            }
        }

        return outList;
    }

    public static Book addBook(Scanner scanner) throws IOException {
        Author author;
        String answer;

        System.out.println("Entrez Le titre du livre :");
        String title = scanner.nextLine();
        System.out.println("Selectionnez le type : (Entrez l'ID)");
        System.out.println("Si le type souhaité n'apparait pas entrez new ou n pour en créer un nouveau :");

        MenuService.showBookTypes();
        EnumBookType[] types = EnumBookType.values();
        EnumBookType bookType = types[Integer.parseInt(scanner.nextLine())];

        System.out.println("Selectionnez l'autheur :");
        System.out.println("Si votre autheur n'apparait pas entrez new ou n pour en créer un nouveau :");
        MenuService.showAuthorList();
        answer = scanner.nextLine();
        if (answer.equals("new") || answer.equals("n")){
            author = addAuthor(scanner);
        }else{
            author = fetchAuthor(Integer.parseInt(scanner.nextLine()));
        }

        System.out.println("Entrez le synopsis :");
        String synopsis = scanner.nextLine();

        System.out.println("Valider ? yes(y) or no(n) :");
        String validation = scanner.nextLine();

        if (validation.equals("yes") || validation.equals("y")){
            Book newBook = new Book();
            newBook.setTitle(title);
            newBook.setBookType(bookType);
            newBook.setAuthor(author);
            newBook.setSynopsis(synopsis);

            String bookString = String.format("%d,%s,%s,%d,%s", newBook.getId(), newBook.getTitle(), newBook.getBookType(), newBook.getAuthor().getId(), newBook.getSynopsis());
            FileService.writeInFile("db/Book.csv", bookString, true);

            System.out.println("Souhaitez vous ajouter ce livre au stock ? yes(y) or no(n) :");
            validation = scanner.nextLine();

            if (validation.equals("yes") || validation.equals("y")){
                System.out.println("Saisissez la quantité :");
                String newStockValue = String.format("%d,%d", newBook.getId(), Integer.parseInt(scanner.nextLine()));

                FileService.writeInFile("db/Stock.csv", newStockValue, true);
            }

            return newBook;
        }

        return null;
    }

    public static void deleteBook(Scanner scanner) throws IOException {

        System.out.println("Quel livre souhaitez supprimer ? (Entrez l'ID)");
        Book book = fetchBook(Integer.parseInt(scanner.nextLine()));

        System.out.println("Etes-vous sur de votre choix ? yes(y) or no(n)");
        String response = scanner.nextLine();

        if (response.equals("yes") || response.equals("y")){
            FileService.deleteFromFile("db/Book.csv", book.getId());
            FileService.deleteFromFile("db/Stock.csv", book.getId());
        }
    }

    public static void addToStock(Scanner scanner) throws IOException {
        System.out.println("Quel livre souhaitez ajouter au stock ? (Entrez l'ID");
        Book book = fetchBook(Integer.parseInt(scanner.nextLine()));

        System.out.println("Saisissez la quantité :");
        Integer quantity = Integer.parseInt(scanner.nextLine());
        String newStockValue = String.format("%d,%d", book.getId(), quantity);

        if (isEntityExistsInFile("db/Stock.csv", book.getId())){
            FileService.updateStockFile(book.getId(), quantity);
        }else {
            FileService.writeInFile("db/Stock.csv", newStockValue, true);
        }
    }

    public static void editStock(Scanner scanner) throws IOException {

        System.out.println("Quelle ligne du stock souhaitez vous modifier ? (Entrez l'ID)");
        Book book = fetchBook(Integer.parseInt(scanner.nextLine()));

        System.out.println("Saisissez la quantité :");
        Integer quantity = Integer.parseInt(scanner.nextLine());
        FileService.updateStockFile(book.getId(), quantity);
    }

    public static void deleteStock(Scanner scanner) throws IOException {
        System.out.println("Quelle ligne southez vous suppprimer ? (Entrez l'ID)");
        Book book = fetchBook(Integer.parseInt(scanner.nextLine()));
        System.out.println("Etes-vous sur de votre choix ? yes(y) or no(n)");
        String response = scanner.nextLine();

        if (response.equals("yes") || response.equals("y")){
            FileService.deleteFromFile("db/Stock.csv", book.getId());
        }
    }

    public static void editBook(Scanner scanner) throws IOException {
        System.out.println("Quel livre souhaitez vous modifier ? (Entrez l'ID");
        Book book = EntityService.fetchBook(Integer.parseInt(scanner.nextLine()));

        String answer;
        System.out.printf("Modifier le titre du livre ? Press ENTER to keep old value : %s%n", book.getTitle());
        answer = scanner.nextLine();

        if (!Objects.equals(answer, "")){
            book.setTitle(answer);
        }

        MenuService.showBookTypes();
        System.out.printf("Modifier le type du livre ? Press ENTER to keep old value : %s or write the ID to get new value%n", book.getTitle());
        answer = scanner.nextLine();

        if (!Objects.equals(answer, "")){
            EnumBookType[] types = EnumBookType.values();
            book.setBookType(types[Integer.parseInt(answer)]);
        }

        MenuService.showAuthorList();
        System.out.printf("Modifier l'auteur du livre ? Press ENTER to keep old value : %s or write the ID to get new value%n", book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
        answer = scanner.nextLine();

        if (!Objects.equals(answer, "")){
            book.setAuthor(fetchAuthor(Integer.parseInt(answer)));
        }

        System.out.printf("Modifier le synopsis du livre ? Press ENTER to keep old value : %s or write the ID to get new value%n", book.getSynopsis());
        answer = scanner.nextLine();

        if (!Objects.equals(answer, "")){
            book.setSynopsis(answer);
        }

        FileService.updateBookFile(book);
    }

    public static Author addAuthor(Scanner scanner) throws IOException {
        System.out.println("Entrez le prénom de l'auteur :");
        String firstName = scanner.nextLine();

        System.out.println("Entre le nom de l'auteur");
        String lastName = scanner.nextLine();

        System.out.println("Valider ? yes(y) or no(n) :");
        String validation = scanner.nextLine();

        if (validation.equals("yes") || validation.equals("y")){
            Author newAuthor = new Author();
            newAuthor.setFirstName(firstName);
            newAuthor.setLastName(lastName);

            String authorString = String.format("%d,%s,%s", newAuthor.getId(), newAuthor.getFirstName(), newAuthor.getLastName());
            FileService.writeInFile("db/Author.csv", authorString, true);
            return newAuthor;
        }

        return null;
    }

    public static void loanBook(Scanner scanner, User user) throws IOException {
        System.out.println("\nQuel livre souhaitez-vous emprunter ? (Entrez l'ID)");
        Book book = fetchBook(Integer.parseInt(scanner.nextLine()));

        Loan newLoan = new Loan();
        newLoan.setBorrower(user);
        newLoan.setBorrowedBook(book);
        newLoan.setStatus(1);

        String loanString = String.format("%d,%d,%d,%d", newLoan.getId(), newLoan.getBorrower().getId(), newLoan.getBorrowedBook().getId(), newLoan.getStatus());

        FileService.writeInFile("db/Loan.csv", loanString, true);
        FileService.updateStockFile(book.getId(), false);
    }

    public static void giveBackLoan(Scanner scanner) throws IOException {
        System.out.println("\nQuel livre souhaitez-vous rendre ? (Entrez l'ID)");
        Loan loanInstance = fetchLoan(Integer.parseInt(scanner.nextLine()));
        loanInstance.setStatus(2);

        String loanString = String.format("%d,%d,%d,%d", loanInstance.getId(), loanInstance.getBorrower().getId(), loanInstance.getBorrowedBook().getId(), loanInstance.getStatus());

        FileService.updateLoanFile(loanInstance);
        FileService.updateStockFile(loanInstance.getBorrowedBook().getId(), true);
    }

    public static ArrayList<String> getAllActiveLoans() throws IOException {

        ArrayList<String> strings = FileService.readFile("db/Loan.csv", true);
        ArrayList<String> userLoans = new ArrayList<>();

        for (String string : strings) {
            String[] columns = string.split(",");

            if (Integer.parseInt(columns[3]) == 1){
                Loan loan = fetchLoan(Integer.parseInt(columns[0]));
                String outString = String.format("ID : %d USER %s %s BOOK : %s STATUS ACTIVE", loan.getId(), loan.getBorrower().getFirstName(), loan.getBorrower().getLastName(), loan.getBorrowedBook().getTitle());
                userLoans.add(outString);
            }
        }

        return userLoans;
    }

    public static ArrayList<String> getAllEndedLoans() throws IOException {
        ArrayList<String> strings = FileService.readFile("db/Loan.csv", true);
        ArrayList<String> userLoans = new ArrayList<>();

        for (String string : strings) {
            String[] columns = string.split(",");

            if (Integer.parseInt(columns[3]) == 2){
                Loan loan = fetchLoan(Integer.parseInt(columns[0]));
                String outString = String.format("ID : %d USER %s %s BOOK : %s STATUS ENDED", loan.getId(), loan.getBorrower().getFirstName(), loan.getBorrower().getLastName(), loan.getBorrowedBook().getTitle());
                userLoans.add(outString);
            }
        }

        return userLoans;
    }
}
