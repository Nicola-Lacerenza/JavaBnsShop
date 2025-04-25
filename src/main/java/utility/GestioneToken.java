package utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Optional;

public class GestioneToken {
    private static final String SECRET_KEY = "OXJX73LCZVhF5gdxJxDPhAPEdgnyK19iOXJX73LCZVhF5gdxJxDPhAPEdgnyK19i"; // Segreto per la firma (almeno 256 bit)
    private static final long EXPIRATION_TIME = 3_600_000L; //3600 secondi cioè 1 ora

    private GestioneToken(){}

    public static String createToken(String email) {
        // Genera una chiave segreta usando la chiave definita
        SecureDigestAlgorithm<SecretKey,SecretKey> algorithm = Jwts.SIG.HS512;
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(),"HmacSHA512");

        // Costruisci e firma il JWT
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1 ora di validità
                .signWith(secretKey,algorithm) // Usa HS256 per la firma
                .compact();
    }

    // Metodo per verificare se il token è valido
    public static String validateToken(String token) {
        Optional<Claims> claims = getClaims(token);
        if(claims.isEmpty()){
            return "";
        }
        //ritorno l'email contenuta nel token che è valido.
        return claims.get().getSubject();
    }

    // Metodo per estrarre le informazioni dal token
    private static Optional<Claims> getClaims(String token) {
        try{
            SecretKey actualKey = new SecretKeySpec(SECRET_KEY.getBytes(),"HmacSHA512");
            Claims claims = Jwts.parser() // Costruisce il parser
                    .verifyWith(actualKey) // Verifica il token con la chiave segreta
                    .build() // Costruisce il parser
                    .parseSignedClaims(token) // Analizza il token e restituisce i claims
                    .getPayload();
            return Optional.of(claims);
        }catch(SignatureException | ExpiredJwtException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
    }
}
