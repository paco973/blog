package com.quest.etna.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quest.etna.model.JwtUserDetails;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	   @Autowired
       private JwtUserDetailsService jwtUserDetailsService;

       @Autowired
       private JwtTokenUtil jwtTokenUtil;

       
       @Autowired
       public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
           this.jwtUserDetailsService = jwtUserDetailsService;
           this.jwtTokenUtil = jwtTokenUtil;
       }
       
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		/*//on récupère le token
		final Optional<String> token = Optional.ofNullable(request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION));
		
		if(token.isPresent() && token.get().startsWith("BEARER")) {
			
	        String bearerToken = token.get().substring("BEARER".length()+1);
		
	        String username = jwtTokenUtil.getUsernameFromToken(bearerToken);
	        
	        request.getRemoteUser().toString();
	        request.getSession();
	        
			UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken (username,"pass");
			
			SecurityContextHolder securityContextHolder;
			
			*/
		

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			/*
			 * try { username = jwtTokenUtil.getUsernameFromToken(jwtToken); } catch
			 * (IllegalArgumentException e) { System.out.println("Unable to get JWT Token");
			 * } catch (ExpiredJwtException e) {
			 * System.out.println("JWT Token has expired"); }
			 */
		} /*
			 * else { logger.warn("JWT Token does not begin with Bearer String"); }
			 */
           
        


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            JwtUserDetails jwtUserDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, jwtUserDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        jwtUserDetails, null, jwtUserDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        
        filterChain.doFilter(request, response);
			
			
			
	}
	
	

}
	
	

