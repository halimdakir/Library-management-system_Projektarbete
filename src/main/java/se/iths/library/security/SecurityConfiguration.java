package se.iths.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.iths.library.securityJwt.filter.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private UserAuthenticationSuccessHandler successHandler;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO Spring security without JWT
        /*http.authorizeRequests()
                .antMatchers("/", "/home", "/home.xhtml", "/item", "/item.xhtml", "/register","/register.xhtml").permitAll()
                .antMatchers("/item/all", "/item/id/**").permitAll()
                .antMatchers("/user/all","/user/id/**").hasRole("ADMIN")
                .antMatchers("/user", "/user.xhtml").hasRole( "USER")
                .antMatchers("/admin", "/admin.xhtml", "/users", "/users.xhtml", "/adminregister.xhtml").hasRole("ADMIN")
                .and()
                .formLogin().successHandler(successHandler).permitAll().and().logout().permitAll();
        http.csrf().disable();*/

        //TODO Spring security with JSON WEB TOKEN
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/", "/home", "/home.xhtml", "/item", "/item.xhtml", "/register","/register.xhtml").permitAll()
                    .antMatchers("/item/all", "/item/id/**").permitAll()
                    .antMatchers("/auth").permitAll().anyRequest().authenticated()
                    .antMatchers("/user/all","/user/id/**").hasRole("ADMIN")
                    .antMatchers("/user", "/user.xhtml").hasRole( "USER")
                    .antMatchers("/admin", "/admin.xhtml", "/users", "/users.xhtml", "/adminregister.xhtml").hasRole("ADMIN")
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .formLogin().successHandler(successHandler).permitAll()
                    .and().logout().invalidateHttpSession(true).clearAuthentication(true).permitAll();
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        //TODO JSON WEB TOKEN
        auth.userDetailsService(userDetailsService);

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

}
