package demo.news;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
	ADMIN("Administrator"),
	EDITOR("Editor");
	
	private final String authority;
}
