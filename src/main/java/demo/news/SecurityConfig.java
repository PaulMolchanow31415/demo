package demo.news;

import demo.news.Repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthorRepository repository;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			val user = repository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			
			val list = user.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getAuthority()))
					.toList();
			
			return UserPrincipal.builder()
					.id(user.getId())
					.username(user.getUsername())
					.password(user.getPassword())
					.authorities(list)
					.build();
		};
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		val provider = new DaoAuthenticationProvider(passwordEncoder());
		provider.setUserDetailsService(userDetailsService());
		return new ProviderManager(provider);
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		return security
				.securityMatcher("/**")
				.authorizeHttpRequests(registry -> registry
						.requestMatchers("/news/edit/*", "/news/create").authenticated()
						.requestMatchers("/news/my").authenticated()
						.anyRequest().permitAll()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/news")
						.permitAll()
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout")
						.permitAll()
				)
				.build();
	}
	
}
