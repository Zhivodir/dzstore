package com.gmail.dzhivchik.config;

import com.gmail.dzhivchik.service.Impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // регистрируем нашу реализацию UserDetailsService
    // а также PasswordEncoder для приведения пароля в формат SHA1
    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getShaPasswordEncoder());
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // включаем защиту от CSRF атак
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/registration", "/createNewUser", "/test").permitAll()
                .antMatchers("/**").hasAnyRole("USER","ADMIN")
                .anyRequest().permitAll()
                .and();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll();

        http.logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true);

    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/fonts/**")
                .antMatchers("/img/**");
    }

    // Указываем Spring контейнеру, что надо инициализировать <b></b>ShaPasswordEncoder
    // Это можно вынести в WebAppConfig, но для понимаемости оставил тут
    @Bean
    public ShaPasswordEncoder getShaPasswordEncoder(){
        return new ShaPasswordEncoder();
    }
}