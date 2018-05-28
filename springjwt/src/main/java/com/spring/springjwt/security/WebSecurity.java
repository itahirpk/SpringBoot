package com.spring.springjwt.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

import static com.spring.springjwt.security.SecurityConstants.SIGN_UP_URL;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL,"/h2_console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//                http.headers().frameOptions().disable(); // for h2 database console to be accessed



    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration configuration = new CorsConfiguration();
        /*
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList(Content-Type", "content-type", "x-requested-with", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "x-auth-token", "x-app-id", "Origin","Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers","Authorization"));
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.applyPermitDefaultValues();
*/
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
        configuration.addExposedHeader("Authorization");
        configuration.applyPermitDefaultValues();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //  source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
      source.registerCorsConfiguration("/**",configuration);
        return source;
    }

}
