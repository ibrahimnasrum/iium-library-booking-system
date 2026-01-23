import model.User;
import model.services.AuthService;
public class AuthTest {
    public static void main(String[] args) {
        User u = AuthService.login("2123456", "password");
        if (u != null) System.out.println("AuthTest: success -> " + u.getMatricNo() + " (" + u.getRole() + ")"); else System.out.println("AuthTest: login failed");
    }
}