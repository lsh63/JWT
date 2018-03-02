package org.xxx.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.xxx.security.jwt.JwtAuthenticationRequest;
import org.xxx.security.jwt.JwtTokenUtil;
import org.xxx.security.jwt.JwtUser;
import org.xxx.security.jwt.JwtUserFactory;
import org.xxx.security.model.Authority;
import org.xxx.security.model.AuthorityName;
import org.xxx.security.model.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * 身份验证接口测试
 * */
@RunWith(SpringRunner.class)//指定使用的单元测试执行类
@SpringBootTest//指明这是一个springboot测试类
public class AuthenticationRestControllerTest {

	//模仿mvc
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    //匿名用户的成功认证
    @Test
    @WithAnonymousUser
    public void successfulAuthenticationWithAnonymousUser() throws Exception {

        JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest("user", "password");

        this.mvc.perform(post("/auth")//执行
                .contentType(MediaType.APPLICATION_JSON)//格式化内容
                .content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest)))//把Java对象转化成json字符串
                .andExpect(status().is2xxSuccessful());
    }

    //角色用户刷新token
    @Test
    @WithMockUser(roles = "USER")
    public void successfulRefreshTokenWithUserRole() throws Exception {

        Authority authority = new Authority();
        authority.setId(0L);
        authority.setName(AuthorityName.ROLE_USER);
        List<Authority> authorities = Arrays.asList(authority);

        User user = new User();
        user.setUsername("username");
        user.setAuthorities(authorities);
        user.setEnabled(Boolean.TRUE);
        user.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

        JwtUser jwtUser = JwtUserFactory.create(user);

        when(this.jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

        when(this.userDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

        when(this.jwtTokenUtil.canTokenBeRefreshed(any(), any())).thenReturn(true);

        this.mvc.perform(get("/refresh")
                .header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a"))
                .andExpect(status().is2xxSuccessful());
    }

    //管理员用户刷新token
    @Test
    @WithMockUser(roles = "ADMIN")
    public void successfulRefreshTokenWithAdminRole() throws Exception {

        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName(AuthorityName.ROLE_ADMIN);
        List<Authority> authorities = Arrays.asList(authority);

        User user = new User();
        user.setUsername("admin");
        user.setAuthorities(authorities);
        user.setEnabled(Boolean.TRUE);
        user.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

        JwtUser jwtUser = JwtUserFactory.create(user);

        when(this.jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

        when(this.userDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

        when(this.jwtTokenUtil.canTokenBeRefreshed(any(), any())).thenReturn(true);

        this.mvc.perform(get("/refresh")
                .header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a"))
                .andExpect(status().is2xxSuccessful());
    }

    //应该在匿名用户的情况下获得授权
    @Test
    @WithAnonymousUser
    public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {

        this.mvc.perform(get("/refresh"))
                .andExpect(status().isUnauthorized());

    }

}

