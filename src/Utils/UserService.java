package Utils;

import Entities.User;
import java.io.IOException;
import java.util.Scanner;

public class UserService {

    public static User userConnection(Scanner scanner) throws IOException {
        System.out.println("Entrez votre identifiant :");
        String login = scanner.nextLine();
        System.out.println("Entrez votre password :");
        String passWord = scanner.nextLine();

        String[] userInfos = new String[]{ login, passWord};
        User user = EntityService.fetchUser(userInfos);

        return user.getId().equals(0) ? null : user;

    }

    public static void userRegistration(Scanner scanner) throws IOException {
        System.out.println("Entrez votre prénom :");
        String firstName = scanner.nextLine();
        System.out.println("Entrez votre nom :");
        String lastName = scanner.nextLine();
        System.out.println("Entrez votre identifiant :");
        String login = scanner.nextLine();
        System.out.println("Entrez votre password :");
        String passWord = scanner.nextLine();

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setLogin(login);
        newUser.setPassword(passWord);

        String userString = String.format("%d,%s,%s,%s,%s", newUser.getId(), newUser.getFirstName(), newUser.getLastName(), newUser.getLogin(), newUser.getPassword());
        FileService.writeInFile("db/User.csv", userString, true);

    }
}
