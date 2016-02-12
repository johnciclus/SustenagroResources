package sustenagro

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.security.core.userdetails.UserDetailsService

//unnecessary if passwordEncoder is defined as `def passwordEncoder`
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers('/admin/**').hasAnyRole('USER', 'ADMIN')
        .antMatchers('/tool/**').hasAnyRole('USER', 'ADMIN')
        .antMatchers('/', '/assets/**').permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
                .loginPage("/login")
                .permitAll()
        .and()
        .logout()
                .permitAll()
    }

    @Autowired
    protected void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("pwd").roles("USER", "ADMIN");
    }
}