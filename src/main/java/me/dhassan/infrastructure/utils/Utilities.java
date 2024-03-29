package me.dhassan.infrastructure.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.mindrot.jbcrypt.BCrypt;

public class Utilities {

    private static final String JWT_SECRET = "123"; // TODO read env variable

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean compareHashedPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public static String createToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    public static String decodeTokenAndReturnSubject(String token) {
        try {
            DecodedJWT decodedJWT = JWT
                    .require(Algorithm.HMAC256(JWT_SECRET))
                    .build()
                    .verify(token);

            return decodedJWT.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
