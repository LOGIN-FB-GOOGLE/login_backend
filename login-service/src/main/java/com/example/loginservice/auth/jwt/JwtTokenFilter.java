package com.example.loginservice.auth.jwt;

import com.example.loginservice.auth.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(req);
            String email = jwtProvider.getEmailFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Object userDetails1;
            Object principal;
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(req, res);
    }

    private String getToken(HttpServletRequest req) {
        String authReq = req.getHeader("Authorization");
        if (authReq != null && authReq.startsWith("Bearer")) {
            return authReq.replace("Bearer", "");
        }
        return null;
    }
}
