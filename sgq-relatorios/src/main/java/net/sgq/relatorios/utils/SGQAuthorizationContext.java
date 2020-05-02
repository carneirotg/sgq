package net.sgq.relatorios.utils;

public class SGQAuthorizationContext {

	private static final ThreadLocal<SGQAuthorizationContext> CTX = new ThreadLocal<>();
	
	private String bearerToken;
	
	public static SGQAuthorizationContext getInstance() {
		SGQAuthorizationContext ctx = CTX.get();
		
		if(ctx == null) {
			ctx = new SGQAuthorizationContext();
			CTX.set(ctx);
		}
		
		return ctx;		
	}

	public String getBearerToken() {
		return bearerToken;
	}

	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}
	
}
