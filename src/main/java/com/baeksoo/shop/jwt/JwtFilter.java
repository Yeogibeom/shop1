package com.baeksoo.shop.jwt;

import com.baeksoo.shop.service.CustomUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("필터 실행됨");
        Cookie[] cookies = request.getCookies();

        // 쿠키가 없을 경우 필터 체인 실행
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // jwt 쿠키 검색
        var jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");

        if (jwtCookie.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("필터: " + jwtCookie);

        Claims claim;
        try {
            claim = JwtUtil.extractToken(jwtCookie);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        // 권한 정보 설정
        var authorities = Arrays.stream(claim.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 사용자 생성 및 인증 설정
        var customUser = new CustomUser(
                claim.get("username").toString(),
                "none",
                authorities
        );
        customUser.displayName = claim.get("displayName").toString();

        var authToken = new UsernamePasswordAuthenticationToken(
                customUser, null, authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터 체인 실행
        filterChain.doFilter(request, response);
    }
}
