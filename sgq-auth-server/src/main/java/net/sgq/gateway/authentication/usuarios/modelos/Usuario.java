package net.sgq.gateway.authentication.usuarios.modelos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.sgq.gateway.authentication.usuarios.modelos.enums.Papeis;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 3303592878275179940L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	private String username;
	@JsonIgnore
	private String displayName;
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Papel> authorities = new ArrayList<>();
	
	public Usuario() {
		super();
	}

	public Usuario(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return Boolean.TRUE;
	}

	@Override
	public boolean isAccountNonLocked() {
		return Boolean.TRUE;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return Boolean.TRUE;
	}

	@Override
	public boolean isEnabled() {
		return Boolean.TRUE;
	}
	
	public List<String> getRoles(){
		return authorities.stream().map(Papel::getRole).collect(Collectors.toList());
	}
	
	public Boolean isInRole(Papeis papel) {
		return getRoles().contains(papel.name());
	}
	
	public void addRole(Papeis papel) {
		this.authorities.add(new Papel(papel.getAuthority()));
	}

}
