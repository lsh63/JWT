package org.xxx.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/*
 * 身份认证入口点
 * */
@Component//这个类交给Spring管理,由于不好说这个类属于哪个层面，就用@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    /*
     * Web服务器收到一个http请求，会针对每个请求创建一个HttpServletRequest和HttpServletResponse对象，
     * 向客户端发送数据找HttpServletResponse,
     * 从客户端取数据找HttpServletRequest.
     * */
    @Override
    public void commence(HttpServletRequest request,//Http请求，包含a.请求地址、b.请求头、c.实体数据
                         HttpServletResponse response,//Http的响应，包含a.响应头、b.状态码、c.实体数据
                         AuthenticationException authException) throws IOException {
        // 当用户试图访问一个安全的REST资源而不提供任何凭证时，就会调用此方法
        // 我们只需要发送401未授权的响应，因为没有“登录页面”可以重定向到
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}