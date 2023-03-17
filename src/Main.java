
import java.io.IOException;
import java.util.Scanner;
import Enums.EnumMenuContext;
import Utils.MenuService;

public class Main {

    public static void main(String[] args) throws IOException {
       boolean isLoggedIn = false;

       Scanner scanner = new Scanner(System.in);
       MenuService.showConnectionMenu(scanner);

       if(MenuService.getCurrentUser() != null){
           isLoggedIn = true;
       }

       if (isLoggedIn){

           if (MenuService.getMenuContext().equals(EnumMenuContext.NONE)){
               MenuService.showMenu(scanner);
           }
       }
    }
}