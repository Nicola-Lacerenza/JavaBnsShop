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
    private static final String SECRET_KEY = "OXJX73LCZVhF5gdxJxDPhAPEdgnyK19iOXJX73LCZVhF5gdxJxDPhAPEdgnyK19i"; // Segreto per la firma (almeno 256 bit)
    private static final long EXPIRATION_TIME = 3600000; // 1 ora

    private GestioneToken(){}

    public static String createToken(String email) {
        // Genera una chiave segreta usando la chiave definita
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + EXPIRATION_TIME;

        // Costruisci e firma il JWT
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(expMillis)) // 1 ora di validità
                .signWith(signingKey, SignatureAlgorithm.HS256) // Usa HS256 per la firma
                .compact();
    }

    // Metodo per estrarre le informazioni dal token
    public static Claims getClaims(String token) {
        return Jwts.parser() // Costruisce il parser
                .setSigningKey(Base64.getDecoder().decode(SECRET_KEY)) // Imposta la chiave segreta
                .build() // Costruisce il parser
                .parseClaimsJws(token) // Analizza il token e restituisce i claims
                .getBody();
    }

    // Metodo per verificare se il token è valido
    public static boolean validateToken(String token, String email) {
        final String tokenEmail = getClaims(token).getSubject();
        return (email.equals(tokenEmail) && !isTokenExpired(token));
    }

    // Metodo per controllare se il token è scaduto
    private static boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
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
