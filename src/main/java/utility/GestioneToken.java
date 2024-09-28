package utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;

import java.util.Date;


public class GestioneToken {

    private GestioneToken(){}

    public String creaToken(){
        String jwt = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 ora
                .signWith(SignatureAlgorithm.HS256, "secretKey")
                .compact();

    }
}
