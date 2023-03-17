package Utils;

import Entities.Loan;
import Entities.User;
import Enums.EnumBookType;
import Enums.EnumMenuContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuService {

    private static EnumMenuContext menuContext = EnumMenuContext.NONE;

    public static User currentUser = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        MenuService.currentUser = currentUser;
    }

    public static EnumMenuContext getMenuContext() {
        return menuContext;
    }

    public static void setMenuContext(EnumMenuContext menuContext) {
        MenuService.menuContext = menuContext;
    }

    public static void showConnectionMenu(Scanner scanner) throws IOException {
        System.out.println("Bonjour, êtes vous deja client ? yes(y) or no(n)");
        String answer = scanner.nextLine().toLowerCase();


        if (answer.equals("y") || answer.equals("yes")) {
            setCurrentUser(UserService.userConnection(scanner));

        } else {
            UserService.userRegistration(scanner);
        }
    }

    public static void selectOption(Scanner scanner ,String selectedOption) throws IOException {

        if (getMenuContext().equals(EnumMenuContext.MAIN_MENU)){
            switch (selectedOption) {
                case "books", "b" -> showBookMenu(scanner);
                case "stock", "s" -> showStockMenu(scanner);
                case "loans", "l" -> showLoanMenu(scanner);
                case "history", "h" -> showHistoryMenu(scanner);
                case "exit", "e" -> {
                    return;
                }
            }
        }

        if (getMenuContext().equals(EnumMenuContext.BOOK_MENU)) {
            switch (selectedOption) {
                case "loan", "l" -> {
                    if (showBookList()){
                        EntityService.loanBook(scanner, getCurrentUser());
                    }
                    askBack(scanner);
                }
                case "add", "a" -> {
                    EntityService.addBook(scanner);
                    askBack(scanner);
                }
                case "edit", "e" -> {
                    if (showBookList()){
                        EntityService.editBook(scanner);
                    }
                    askBack(scanner);
                }
                case "delete", "d" -> {
                    if (showBookList()) {
                        EntityService.deleteBook(scanner);
                    }
                    askBack(scanner);
                }
                case "back", "b" -> showMenu(scanner);
            }
        }

        if (getMenuContext().equals(EnumMenuContext.STOCK_MENU)) {
            switch (selectedOption) {
                case "consult", "c" -> {
                    showStockList();
                    askBack(scanner);
                }
                case "add", "a" -> {
                    if (showBookList()){
                        EntityService.addToStock(scanner);
                    }
                    askBack(scanner);
                }
                case "edit", "e" -> {
                    if (showStockList()) {
                        EntityService.editStock(scanner);
                    }
                    askBack(scanner);
                }
                case "delete", "d" -> {
                    if (showStockList()){
                        EntityService.deleteStock(scanner);
                    }
                    MenuService.askBack(scanner);
                }
                case "back", "b" -> showMenu(scanner);
            }
        }

        if (getMenuContext().equals(EnumMenuContext.LOAN_MENU)) {
            switch (selectedOption) {
                case "consult", "c" -> {
                    showUserLoans(false);
                    askBack(scanner);
                }
                case "all", "a" -> {
                    showLoans();
                    askBack(scanner);
                }
                case "new", "n" -> {
                    if (showBookList()){
                        EntityService.loanBook(scanner, getCurrentUser());
                    }
                    askBack(scanner);
                }
                case "give", "g" -> {
                    if (showUserLoans(true)){
                        EntityService.giveBackLoan(scanner);
                    }
                    askBack(scanner);
                }
                case "back", "b" -> showMenu(scanner);
            }
        }

        if (getMenuContext().equals(EnumMenuContext.ASK_BACK)) {
            switch (selectedOption) {
                case "yes", "y" -> showMenu(scanner);
            }
        }
    }

    public static void showMenu(Scanner scanner) throws IOException {
        String[] options = new String[]{
                "--> books(b)   : Books",
                "--> stock(s)   : Stock",
                "--> loans(l)   : Loan",
                "--> exit(e)    : Exit"
        };


        System.out.println(" ================================");
        System.out.println(" ============= MENU =============");
        System.out.println(" ================================");

        for (String option : options) {
            System.out.println(option);
        }

        setMenuContext(EnumMenuContext.MAIN_MENU);
        showMenuAsker(scanner);
    }

    public static void showBookMenu(Scanner scanner) throws IOException {
        String[] options = new String[]{
                "--> loan(l)    : Loan Book",
                "--> add(a)     : Add Book",
                "--> edit(e)    : Edit Book",
                "--> delete(d)  : Delete Book",
                "--> back(b)    : Go back to Main Menu",
        };


        System.out.println(" ===============================");
        System.out.println(" ============ BOOKS ============");
        System.out.println(" ===============================");

        for (String option : options) {
            System.out.println(option);
        }

        setMenuContext(EnumMenuContext.BOOK_MENU);
        showMenuAsker(scanner);
    }

    public static void showStockMenu(Scanner scanner) throws IOException {
        String[] options = new String[]{
                "--> consult(c) : Consult Stock",
                "--> add(a)     : Add to Stock",
                "--> edit(e)    : Edit Stock",
                "--> delete(d)  : Delete from Stock",
                "--> back(b)    : Go back to Main Menu",
        };


        System.out.println(" ===============================");
        System.out.println(" ============ STOCK ============");
        System.out.println(" ===============================");

        for (String option : options) {
            System.out.println(option);
        }

        setMenuContext(EnumMenuContext.STOCK_MENU);
        showMenuAsker(scanner);
    }

    public static void showLoanMenu(Scanner scanner) throws IOException {
        String[] options = new String[]{
                "--> consult(c) : Consult My Loans",
                "--> all(a)     : Consult all Loans",
                "--> new(n)     : Make new loan",
                "--> give(g)    : Give Back My loan",
                "--> back(b)    : Go back to Main Menu",
        };


        System.out.println(" ===============================");
        System.out.println(" ============ LOANS ============");
        System.out.println(" ===============================");

        for (String option : options) {
            System.out.println(option);
        }

        setMenuContext(EnumMenuContext.LOAN_MENU);
        showMenuAsker(scanner);
    }

    public static void showHistoryMenu(Scanner scanner) throws IOException {
        String[] options = new String[]{
                "--> consult(c) : Consult History",
                "--> add(a)     : Add to Stock",
                "--> edit(e)    : Edit Stock",
                "--> delete(d)  : Delete from Stock",
                "--> back(b)    : Go back to Main Menu",
        };


        System.out.println(" ===============================");
        System.out.println(" =========== HISTORY ===========");
        System.out.println(" ===============================");

        for (String option : options) {
            System.out.println(option);
        }

        setMenuContext(EnumMenuContext.HISTORY_MENU);
        showMenuAsker(scanner);
    }

    public static boolean showBookList() throws IOException {
        System.out.println(" ===============================");
        System.out.println(" ========== ALL BOOKS ==========");
        System.out.println(" ===============================");

        ArrayList<String> strings = FileService.readBookFile();

        if (strings.size() > 1){

            for (String string: strings) {
                System.out.println(string);
            }
            return true;
        }
        System.out.println("Il n'y a aucun livre pour le moment");
        return false;

    }

    public static boolean showStockList() throws IOException {
        System.out.println(" ===============================");
        System.out.println(" ============ STOCK ============");
        System.out.println(" ===============================");

        ArrayList<String> strings = FileService.readStockFile();

        if (strings.size() > 1){
            for (String string: strings) {
                System.out.println(string);
            }

            return true;
        }
        System.out.println("Il ny a pas de stock pour le moment");
        return false;
    }

    public static void showAuthorList() throws IOException {
        System.out.println(" ===============================");
        System.out.println(" ========= ALL AUTHORS =========");
        System.out.println(" ===============================");

        ArrayList<String> strings = FileService.readFile("db/author.csv", true);


        for (String string: strings) {
            System.out.println(string);
        }
    }

    public static void showMenuAsker(Scanner scanner) throws IOException {
        System.out.println("\nQue voulez-vous faire ?");
        String response = scanner.nextLine().toLowerCase();
        selectOption(scanner, response);
    }

    public static void showBookTypes(){
        System.out.println(" ===============================");
        System.out.println(" ========== ALL TYPES ==========");
        System.out.println(" ===============================");

        EnumBookType[] types = EnumBookType.values();
        for (EnumBookType type: types) {
            System.out.println(" ID : " + type.ordinal() + " : " + type);
        }
    }

    public static boolean showUserLoans(boolean showOnlyActive) throws IOException {
        System.out.println("================================");
        System.out.println("=========== MY LOANS ===========");
        System.out.println("================================");

        ArrayList<Loan> loans = getCurrentUser().getLoans();

        if (loans.size() > 0){
            if(showOnlyActive){
                for (Loan loan: loans) {
                    if (loan.getStatus() == 1){
                        System.out.printf("ID : %s BOOK : %s\n", loan.getId(), loan.getBorrowedBook().getTitle());
                    }
                }
            }else{
                System.out.println("\n========= ACTIVE LOANS =========");

                for (Loan loan: loans) {
                    if (loan.getStatus() == 1){
                        System.out.printf("ID : %s BOOK : %s\n", loan.getId(), loan.getBorrowedBook().getTitle());
                    }
                }

                System.out.println("\n========== ENDED LOANS ==========");

                for (Loan loan: loans) {
                    if (loan.getStatus() == 2){
                        System.out.printf("ID : %s BOOK : %s\n", loan.getId(), loan.getBorrowedBook().getTitle());
                    }
                }
            }

            return true;
        }
        System.out.printf("Vous n'avez aucun pret");
        return false;
    }

    public static boolean showLoans() throws IOException {
        System.out.println("=================================");
        System.out.println("=========== ALL LOANS ===========");
        System.out.println("=================================");

        ArrayList<String> activeLoans = EntityService.getAllActiveLoans();
        ArrayList<String> endedLoans = EntityService.getAllEndedLoans();


        if (activeLoans.size() > 1 || endedLoans.size() > 1){
            System.out.println("\n========= ACTIVE LOANS =========");

            if (activeLoans.size() > 1){
                for (String string: activeLoans) {
                    System.out.println(string);
                }
            }else {
                System.out.println("Il n'y a aucun pret actif pour le moment");
            }


            System.out.println("\n========== ENDED LOANS ==========");

            if (endedLoans.size() > 1) {
                for (String string : endedLoans) {
                    System.out.println(string);
                }
            }else {
                System.out.println("Il n'y a aucun pret terminé pour le moment");
            }

            return true;

        }

        System.out.println("Il n'y a aucun pret pour le moment");
        return false;

    }

    public static void askBack(Scanner scanner) throws IOException {
        setMenuContext(EnumMenuContext.ASK_BACK);
        System.out.println("\nVoulez vous faire autre chose ? yes(y) or no(n)");
        String answer = scanner.nextLine();
        selectOption(scanner, answer);
    }
}
