package UserManagement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Hashing
{
    static MessageDigest messageDigest;

    static
    {
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    public static String username(String username){ return getUsername(username); }

    public static String getUsername(String username)
    {
        return hashedValue(username);
    }

    public static String password(String password)
    {
        return getPassword(password);
    }

    public static String getPassword(String password){
        return hashedValue(password);
    }

    private static String hashedValue(String rawData) {
        messageDigest.update(rawData.getBytes());
        byte[] digest = messageDigest.digest();
        BigInteger no = new BigInteger(1, digest);
        String hashText = no.toString(16);
        while(hashText.length() < 32 )
        {
            hashText = "0" + hashText;
        }
        return hashText;
    }
}
