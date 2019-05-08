package com.shopdirect.forecasting.authorization.config;

import com.shopdirect.forecasting.authorization.repository.AppUserRepository;
import com.shopdirect.forecasting.authorization.service.DefaultAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(
            AuthenticationManagerBuilder auth,
            AppUserRepository appUserRepository) throws Exception {
        auth.authenticationProvider(new DefaultAuthenticationProvider(appUserRepository));
    }

    // Required for H2 Console to work
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.headers().frameOptions().sameOrigin();
//        http.csrf().ignoringAntMatchers("/h2-console/**");
//        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
//    }

}
