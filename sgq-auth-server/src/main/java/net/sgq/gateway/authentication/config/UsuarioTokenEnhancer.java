package net.sgq.gateway.authentication.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import net.sgq.gateway.authentication.usuarios.modelos.Usuario;
import net.sgq.gateway.authentication.usuarios.services.UsuarioService;

@Service
public class UsuarioTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UsuarioService service;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		final Usuario usuario = service.consultarUsuario(authentication.getName());
		final Map<String, Object> sgqClaims = new HashMap<>(accessToken.getAdditionalInformation());
		
		sgqClaims.put("display_name", usuario.getDisplayName());
		
		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(sgqClaims);
		
		return accessToken;
	}

}
