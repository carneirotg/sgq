package net.sgq.incidentes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class SecurityConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/**").fullyAuthenticated()
				.and().cors()
				.and().csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.anonymous().disable();
	}
	
}
