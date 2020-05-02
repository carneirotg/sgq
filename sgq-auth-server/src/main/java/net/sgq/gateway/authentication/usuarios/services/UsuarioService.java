package net.sgq.gateway.authentication.usuarios.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.sgq.gateway.authentication.usuarios.modelos.Usuario;
import net.sgq.gateway.authentication.usuarios.modelos.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario u = repository.findByUsername(username);
		
		if(u == null) {
			throw new UsernameNotFoundException(String.format("Usuário '%s' não encontrado", username));
		}
		
		return u;
	}

}
