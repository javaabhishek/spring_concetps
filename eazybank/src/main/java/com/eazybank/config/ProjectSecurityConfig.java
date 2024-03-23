package com.eazybank.config;

import com.eazybank.filter.CsrfCookieFilter;
import com.eazybank.filter.JWTTokenGeneratorFilter;
import com.eazybank.filter.JWTTokenValidatorFilter;
import com.eazybank.filter.RequestValidationBeforeFilter;
import com.eazybank.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        /*
          thru below code we are telling spring boot application that, create JSESSIONID and send it to UI app
          then UI APP will send this JSESSIONID back to server from subsequent request

          http.securityContext((context) -> context
                        .requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

                But now I don;t want to use JSESSIONID to create, for that I need to tell
                spring boot to don't create it by using below way

                http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

         */

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http/*.securityContext((context) -> context
                        .requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))*/
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                            CorsConfiguration cfg=new CorsConfiguration();
                            cfg.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                            cfg.setAllowedMethods(Collections.singletonList("*"));
                            cfg.setAllowedHeaders(Collections.singletonList("*"));
                            cfg.setExposedHeaders(Collections.singletonList("Authorization"));//Authorization
                            // header is added to append JTW token in response object when the server
                            //create it.
                            cfg.setAllowCredentials(true);
                            cfg.setMaxAge(3600L);
                            return cfg;
                        }
                    });
                })
                //.csrf((csrf)-> csrf.disable())
                //.csrf((csrf)->csrf.ignoringRequestMatchers("/contact","/register"))
                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/contact", "/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)//register JWTTokenGeneratorFilter
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) ->
                requests.requestMatchers("/contact","/notices","/register").permitAll()
                        //using authority
                        //.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                        //.requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT","VIEWBALANCE")
                        //.requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                        //.requestMatchers("/myCards").hasAuthority("VIEWCARDSDETAIL")
                        //.requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                        //using role
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                        //.requestMatchers("/myLoans").hasRole("USER") //commenting this rest end point as putting securing it using method level secutiry.
                        //.requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers("/myCards").hasRole("MANAGER")//for -ve scenario
                .anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

  /*  @Bean
    public InMemoryUserDetailsManager userDetailsManager(){

        /*
        InMemoryUserDetailsManager implementation
        Approach -1:
        UserDetails adminUser= User.withDefaultPasswordEncoder()
                .username("admin")
                .password("1234")
                .authorities("admin")
                .build();

        UserDetails normalUser= User.withDefaultPasswordEncoder()
                .username("user")
                .password("1234")
                .authorities("user")
                .build();

        return new InMemoryUserDetailsManager(adminUser,normalUser);

        */
        //Approach -2: not using .withDefaultPasswordEncoder method instead if creating bean of NoOpPasswordEncoder
       /* UserDetails adminUser= User.withUsername("admin")
                .password("1234")
                .authorities("admin")
                .build();

        UserDetails normalUser= User.withUsername("user")
                .password("1234")
                .authorities("user")
                .build();

        return new InMemoryUserDetailsManager(adminUser,normalUser);
    }
    */

    //Approach -2: Part of Approach -2:
   /* @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

  /*  @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }*/

    //provide own implementation like JdbcUserDetailsManager

   /* @Bean >> Do this if you won't write @service /@component annotation on EazyBankUserDetailsManager
    public EazyBankUserDetailsManager eazyBankUserDetailsManager(CustomerRepository customerRepository){
        return new EazyBankUserDetailsManager(customerRepository);
    }*/

}
