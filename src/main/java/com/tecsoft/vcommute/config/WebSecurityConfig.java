package com.tecsoft.vcommute.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.tecsoft.vcommute.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/", "/forgetpass", "/assets/**", "/fetchcompanyname", "/monthlyvisit",
                                "/barchartdata",
                                "/endlocation-areaName", "/stlocation-areaName", "/resetPass", "/forgopass","/mobile/**")
                        .permitAll()
                        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/fetchstate","/privacy","/takeAttendanceData").permitAll()
                        .antMatchers("/dashboard", "/viewuser", "/location", "/customer", "/usertype", "/approveorder",
                                "/searchCity", "/searchCustomer", "/approval", "/approveOverView",
                                "/UnfinishTravelData",
                                "/report", "/company", "/customer", "/modetransport", "/resetPassword",
                                "/fetchUserByCompanyId",
                                "/addLevelDes", "/fetchGrade", "/fetchLevelDescriptions", "/editLevelDescriptions",
                                "/deleteLevelDescriptions")
                        .hasAnyAuthority("ADMIN", "SUPERADMIN", "HOD", "BACKOFFICE", "FINANCE", "HR")
                        .antMatchers("/usereg").hasAuthority("ADMIN")
                        .antMatchers("/totalUser", "/fetchloginDetails", "/UnfinishedTravelData",
                                "/UnfinishedTravelDataByDate",
                                "/reportGen")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/").permitAll());
    }

}
