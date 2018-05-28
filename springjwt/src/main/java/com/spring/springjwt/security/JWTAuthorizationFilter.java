package com.spring.springjwt.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.spring.springjwt.security.SecurityConstants.HEADER_STRING;
import static com.spring.springjwt.security.SecurityConstants.TOKEN_PREFIX;
import static com.spring.springjwt.security.SecurityConstants.SECRET;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;

        if (header == null || !header.startsWith(TOKEN_PREFIX))
        {
            chain.doFilter(req, res);
            return;
        }
        if (header != null || header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "");

            try {
                String user = Jwts.parser()
                        .setSigningKey(SECRET.getBytes())
                        .parseClaimsJws(authToken.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .getSubject();
            } catch (IllegalArgumentException e) {

                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            } catch (SignatureException e) {
                logger.error("Authentication Failed. Username or Password not valid.");
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);


        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user,null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}