package net.sgq.transparencia.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Encoder;
import feign.form.FormEncoder;

@Configuration
public class FeignConfig {

	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;
	
	@Value("${sgq.authorizationServer.credential.username}")
	private String credentialUsername;
	
	@Value("${sgq.authorizationServer.credential.password}")
	private String credentialPassword;

	@Bean
	@Primary
	@Scope("prototype")
	public Encoder feignFormEncoder() {
		return new FormEncoder(new SpringEncoder(this.messageConverters));
	}
	
	@Bean
	public BasicAuthRequestInterceptor basicAuth() {
		return new BasicAuthRequestInterceptor(credentialUsername, credentialPassword);
	}

}
