package com.italooliveira.projeto.todo_list.security;

import com.italooliveira.projeto.todo_list.services.TokenService;
import com.italooliveira.projeto.todo_list.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        var token = this.recoverToken(request);
        
        if (token != null) {
            var login = tokenService.validateToken(token);
            
            if (login != null) {
                // Aqui usamos o serviço para buscar o usuário
                UserDetails user = userDetailsService.loadUserByUsername(login);
                
                // Criamos o objeto de autenticação que o Spring Security entende
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                
                // "Carimbamos" o contexto de segurança: a partir daqui, o usuário está LOGADO
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // Segue para o próximo filtro (ou para o Controller)
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}