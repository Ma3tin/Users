import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

@Named
@RequestScoped
public class UserService {

    private final Random RANDOM = new SecureRandom();
    private final int ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;

    @Inject
    private UserRepository ur;

    @Inject
    private SavedUserBean sub;
    public String getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    public void createUser(String fullName, String email, String password) {
        String salt =  getNextSalt();
        String hashedPassword = DigestUtils.sha3_256Hex(password + salt);
        hashedPassword = hashedPassword + salt;
        ur.createUser(fullName, email, hashedPassword);
    }


    public String getSalt(String hashedPasswordWithSalt) {
        return hashedPasswordWithSalt.substring(hashedPasswordWithSalt.length() - 16);
    }
    public String getPasswordWithoutSalt(String hashedPasswordWithSalt) {
        return hashedPasswordWithSalt.substring(0, hashedPasswordWithSalt.length() - 16);
    }

    public void getUser(String email, String password) throws IOException {
        User user = ur.getUser(email);
        String salt = getSalt(user.getPassword());
        String hashedPassword = DigestUtils.sha3_256Hex(password + salt);
       if (getPasswordWithoutSalt(user.getPassword()).equals(hashedPassword)) {
            sub.setCurrentUser(user);
           FacesContext.getCurrentInstance().getExternalContext().redirect("/Users/hiddenPage.xhtml");
       }
    }

    public void logOut() {
        sub.setCurrentUser(null);
    }
}