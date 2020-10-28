package com.sg.blogcms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetails;

    /**
     * Ensure passwords are coded before written to db
     *
     * @param auth {AuthenticationManagerBuilder} to create authentication
     * @throws Exception if errors on adding authentication
     */
    @Autowired
    public void configureGlobalInDB(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * Initialize password encoder
     *
     * @return {BCryptPasswordEncoder} for use as a spring bean
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure web security setup for domain
     *
     * @param http {HttpSecurity}
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN") //admin control panel
                .antMatchers("/deletePost").hasRole("ADMIN")
                .antMatchers("/deleteUser").hasRole("ADMIN")
                .antMatchers("/editCategory").hasRole("ADMIN")
                .antMatchers("/editPost").hasRole("ADMIN")
                .antMatchers("/editUser").hasRole("ADMIN")
                .antMatchers("/postManagement").hasRole("ADMIN")
                .antMatchers("/createCategory").hasAnyRole("CREATOR", "ADMIN")
                .antMatchers("/createPost").hasAnyRole("CREATOR", "ADMIN")
                .antMatchers("/", "/home", "/login", "/blog", "/viewPost").permitAll()
                .antMatchers("/styles/**", "/js/**", "/fonts/**", "/images/**").permitAll()
                //login form submissions
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?login_error=1") //if login fails, go to this url
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/") //on log out, go to base page
                .permitAll();
    }

}
