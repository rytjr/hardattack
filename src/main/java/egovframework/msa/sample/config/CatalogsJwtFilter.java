package egovframework.msa.sample.config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CatalogsJwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CatalogsJwtFilter.class);

    @Value("${jwt.secret}")
    private String secret;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (containsAuthorization(request)) {
            String token = resolveToken(request);
            if (validateToken(token)) {
                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
	}

    private boolean containsAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Catalog Error : 잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Catalog Error : 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Catalog Error : 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("Catalog Error : JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

}
