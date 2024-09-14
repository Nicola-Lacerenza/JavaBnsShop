package utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class Crittografia {

    private Crittografia(){}

    public static Optional<String> get_SHA_512_SecurePassword(String passwordToHash,String salt){
        if(passwordToHash == null || salt == null){
            return Optional.empty();
        }
        if(passwordToHash.isEmpty() || salt.isEmpty()){
            return Optional.empty();
        }
        String generatedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
            return Optional.of(generatedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
