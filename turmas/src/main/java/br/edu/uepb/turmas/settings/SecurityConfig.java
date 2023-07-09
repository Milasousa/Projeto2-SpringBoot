package br.edu.uepb.turmas.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/signup",
            "/h2-console/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.cors().and().csrf().disable();
        http.headers().frameOptions().sameOrigin();
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .mvcMatchers(HttpMethod.POST,"/alunos/**").permitAll()
                .mvcMatchers(HttpMethod.POST,"/professores/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/projetos/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/alunos/**").permitAll()
                .mvcMatchers("/projetos/**").hasAuthority("ROLE_PROFESSOR")
                .mvcMatchers("/professores/**").hasAuthority("ROLE_PROFESSOR")
                .mvcMatchers("/alunos/**").hasAuthority("ROLE_PROFESSOR")
                .anyRequest().authenticated();
    }
}