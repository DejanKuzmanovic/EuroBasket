package org.unibl.eurobasket.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/all").hasRole("ADMIN")
                .antMatchers("/user/create").hasRole("ADMIN")
                .antMatchers("/user/delete/**").hasRole("ADMIN")
                .antMatchers("/user/edit").hasRole("USER")
                .antMatchers("/user/premium").hasRole("USER")
                .antMatchers("/game/create").hasRole("MODERATOR")
                .antMatchers("/game/edit").hasRole("MODERATOR")
                .antMatchers("/game/activate/**").hasRole("MODERATOR")
                .antMatchers("/game/finish/**").hasRole("MODERATOR")
                .antMatchers("/game/active").hasRole("USER")
                .antMatchers("/game/finished").hasRole("USER")
                .antMatchers("/game/score/**").hasRole("USER")
                .antMatchers("/news/create").hasRole("MODERATOR")
                .antMatchers("/news/delete/**").hasRole("MODERATOR")
                .antMatchers("/news/comment/delete/**").hasRole("MODERATOR")
                .antMatchers("/news/all").hasRole("USER")
                .antMatchers("/news/comment/create/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutUrl("/logout");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
