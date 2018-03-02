package org.xxx.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xxx.security.jwt.JwtAuthenticationRequest;
import org.xxx.security.jwt.JwtTokenUtil;
import org.xxx.security.jwt.JwtUser;
import org.xxx.security.service.JwtAuthenticationResponse;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders="Authorization")
@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /*
     * 登录
     * http://127.0.0.1:8080/auth
     * */
    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
    		@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) 
    				throws AuthenticationException {

        // Perform the security安全验证
        final Authentication authentication = authenticationManager.authenticate(//生成验证
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),//从请求中获得username
                        authenticationRequest.getPassword()//从请求中获得password
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);//验证录入

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(
        		authenticationRequest.getUsername());//生成userDetails
        final String token = jwtTokenUtil.generateToken(userDetails, device);//生成token,device是生成spring策略

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    /*
     * 刷新token
     * http://127.0.0.1:8080/refresh
     * */
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);//获得请求头并赋值给authToken
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            // Return the new token
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
