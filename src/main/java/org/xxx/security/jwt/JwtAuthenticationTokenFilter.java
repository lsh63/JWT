package org.xxx.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/*
 * 身份认证Token过滤器
 * */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")//获取properties文件中的配置值，jwt.header: Authorization赋给变量tokenHeader = Authorization
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    	
        final String requestHeader = request.getHeader(this.tokenHeader);

        String username = null;//登录用户名（用户）
        String authToken = null;//声明通行证（申请通行证）
        
        // 如果请求头不为null且开头为"Bearer "
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
        	//截取掉"Bearer "（制作通行证）
            authToken = requestHeader.substring(7);//截取掉requestHeader对象字符串从首字母起长度为7的字符串，返回剩余字符串赋值给authToken
            try {
            	//验证通行证（是否 人&证 统一）
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }

        logger.info("checking authentication for user " + username);
        
        //如果username不为null
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 从数据库加载userDetails并不是必需的。您也可以将信息存储在Token中并从中读取，这取决于你
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 对于简单的验证，只需检查标记完整性就足够了。你不一定需要强制调用数据库。这取决于你
            // 如果通行证未过期
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
            	//声明认证处理对象authentication（相当于安检员收录通关信息）
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                //将authentication保存在Security上下文中（通关信息录入安全管理系统中）
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);//到下一个链（请前往下一关）
    }
}