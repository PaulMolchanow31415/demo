package demo.news.Model;

import demo.news.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String username;
	private String email;
	private String password;
	
	@ElementCollection(fetch = EAGER, targetClass = Role.class)
	private Set<Role> roles;
	
	@OneToMany(cascade = ALL, mappedBy = "author")
	private Set<News> news;
}
