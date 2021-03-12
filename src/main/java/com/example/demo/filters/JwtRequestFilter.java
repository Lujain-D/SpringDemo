package com.example.demo.filters;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.services.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        System.out.println("in filter");
        String username = null;
        String jwt = null;

        if( authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            System.out.println("start reading and extracting token");
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            System.out.println(username);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication()==null){
            System.out.println("user and context exist");
//            try{
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("got User Details " + userDetails.getAuthorities());
                if(jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()

                    );
                    System.out.println("token is valid");
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
//            }
//            catch (Exception e){
//                System.out.println("catching in filter");
//            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
