package net.sgq.gateway.authentication.usuarios.modelos;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "papeis")
public class Papel implements GrantedAuthority {

	private static final long serialVersionUID = 1080297243394405678L;

	@Id
	private String authority;
	
	public Papel() {
		super();
	}
	
	public Papel(String authority) {
		super();
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
	
	public String getRole() {
		return authority.replace("ROLE_", "");
	}

}
