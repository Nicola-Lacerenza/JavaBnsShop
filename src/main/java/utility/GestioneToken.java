package utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

public class GestioneToken {
    private GestioneToken(){}

    public String createToken(String email){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 3600000;
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(("OXJX73LCZVhF5gdxJxDPhAPEdgnyK19i").getBytes());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(new Date(expMillis)) // 1 ora
                .signWith(signatureAlgorithm,signingKey)
                .compact();
    }

    public static Optional<Claims> decodeJWT(String jwt) {
        String[] chunks = jwt.split("\\.");
        if(chunks.length < 2){
            return Optional.empty();
        }
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        return Optional.empty();
    }
}
