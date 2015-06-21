package hello.dcsms.plak.Frgmnt;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;

import hello.dcsms.plak.Utils.Encryption;

/**
 * Created by jmkl on 4/28/2015.
 */
public class ActFrag {
    static byte[] iv = {-89, -19, 17, -83, 86, 106, -31, 30, -5, -111, 61, -75, -84, 95, 120, -53};

    public static Encryption getTools(String key, String salt) {
        Encryption encryption=null;
        try {
            encryption = new Encryption.Builder()
                    .setKeyLength(128)
                    .setKey(key)
                    .setSalt(salt)
                    .setIv(iv)
                    .setCharsetName("UTF8")
                    .setIterationCount(65536)
                    .setDigestAlgorithm("SHA1")
                    .setBase64Mode(Base64.DEFAULT)
                    .setAlgorithm("AES/CBC/PKCS5Padding")
                    .setSecureRandomAlgorithm("SHA1PRNG")
                    .setSecretKeyType("PBKDF2WithHmacSHA1")
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryption;
    }


}
