package impress.weasp.infra.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            System.out.println("Processando requisição para: " + path);

            if (path.startsWith("/auth/")) {
                System.out.println("Rota pública, pulando autenticação");
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("Token extraído: " + request.getHeader("Authorization"));
            String token = resolveToken(request);
            System.out.println("Token extraído: " + token);
            System.out.println("Token extraído: " + (token != null ? "presente" : "ausente"));

            if (token != null) {
                try {

                    String email = jwtTokenProvider.validateToken(token);
                    System.out.println("Token validado para email: " + email);

                    String authorities = jwtTokenProvider.getRoleFromToken(token);
                    System.out.println("Authorities extraídas: " + authorities);
                    List<GrantedAuthority> auths = AuthorityUtils
                            .commaSeparatedStringToAuthorityList((String) authorities);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Autenticação definida com sucesso");
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    System.err.println("Erro na autenticação: " + e.getMessage());
                    throw e;
                }
            }
            System.out.println("Continuando com a cadeia de filtros");
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Retorna o token sem o prefixo "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}

