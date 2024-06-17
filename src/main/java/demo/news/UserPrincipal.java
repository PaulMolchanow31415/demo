package demo.news;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
public class UserPrincipal implements UserDetails {
	private Long id;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
}
