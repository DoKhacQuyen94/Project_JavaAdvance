package cyper.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
    public static String HashUtil(String password) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
//        System.out.println("BCrypt hash: " + hash);
        return hash;
    }
    public static boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
