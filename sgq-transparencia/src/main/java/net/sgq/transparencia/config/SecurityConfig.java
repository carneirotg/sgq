package net.sgq.transparencia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/**").fullyAuthenticated()
				.antMatchers(HttpMethod.GET, "/**/eventos/**").permitAll()
				.and().cors()
				.and().csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.anonymous().disable();
	}}
