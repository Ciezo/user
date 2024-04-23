/**
 * <p>
 *     JSON web tokens (JWTs) claims are pieces of information asserted about a subject. For example,
 *     an ID token (which is always a JWT) can contain a claim called name that asserts that the name
 *     of the user authenticating is "John Doe"
 * </p>
 *
 * <p>
 *     Example:
 *     {
 *       "sub": "1234567890",
 *       "name": "John Doe",
 *       "admin": true
 *     }
 * </p>
 *
 * <p>
 *     Reference: https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-token-claims
 * </p>
 */
package com.userservice.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    AESKeyGenerator aesKeyGenerator = new AESKeyGenerator(256);
    private final String SECRET_SECRET_KEY = aesKeyGenerator.getSecretKey();
    private String username;
    /* The attribute, password, is not included because we don't want to generate it
     * along with our response token from the AuthenticationResponse */



    /**
     * Transform SECRET_SECRET_KEY into bytes (HMAC)
     * @return keys in bytes
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * Extracts all Claims based on the generated token
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    /**
     *
     * @param token to be resolved
     * @param resolvedClaims
     * @return The resolved single claim based on the token from the payload.
     * @param <T> generic type
     */
    public <T> T extractSingleClaim(String token, Function<Claims, T> resolvedClaims) {
        /* Extract all claims from the token, and then return the resolved claims */
        final Claims claims = extractAllClaims(token);
        return resolvedClaims.apply(claims);
    }



    /**
     * To build our token, we need to get a value from the
     * attribute, username, it can be used to identify our logged-on user
     * @param token based on the header of the given request from user.
     * @return the "username" from the payload
     */
    public String extractUsername(String token) {
        username = extractSingleClaim(token, Claims::getSubject);           // Subject here is "username"
        return username;
    }



    /**
     * Use this method to create a token for the logged-in user who has three days until session expiration
     * @param extraClaims which are the sub, iat, exp
     * @param userDetails <code>username</code> or <code>email</code> to identify the logged-on user
     * @return generated token for the Signing-in user
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 72))       // three days expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    /**
     * Use this method to generate token based on UserDetails only without using Claims
     * @param userDetails such as firstname, lastname, username, email, password
     * @return generated token for the Signing-in user
     */
    public String generateToken(UserDetails userDetails) {
        /* Store the token in a HashMap format */
        return generateToken(new HashMap<>(), userDetails);
    }



    /**
     * Checks the generated token if it is valid, and belongs to the authenticated user
     * @param token is the token to check against the user to be authenticated
     * @param userDetails is the information collected about the user
     * @return <code>false</code> if token does not belong to the user. Otherwise, <code>true</code> if it is.
     */
    public boolean isTokenGenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }



    /**
     * Checks if the given token is expired or not
     * @param token to check
     * @return <code>false</code> if token is expired. Otherwise, <code>true</code> if not
     */
    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }



    public Date extractExpirationDate(String token) {
        return extractSingleClaim(token, Claims::getExpiration);
    }

}
