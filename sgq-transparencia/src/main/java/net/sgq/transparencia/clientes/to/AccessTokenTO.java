package net.sgq.transparencia.clientes.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessTokenTO {

	@JsonProperty("access_token")
	private String accessToken;

	public AccessTokenTO() {
		super();
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}