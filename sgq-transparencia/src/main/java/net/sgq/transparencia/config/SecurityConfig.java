package net.sgq.transparencia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter /*ResourceServerConfigurerAdapter*/ {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/**").authenticated()
				.and().cors()
				.and().csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.anonymous().disable();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.GET, "/eventos");
	}


}
