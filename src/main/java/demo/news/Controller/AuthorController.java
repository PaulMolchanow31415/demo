package demo.news.Controller;

import demo.news.Model.Author;
import demo.news.Repository.AuthorRepository;
import demo.news.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
	
	private final AuthorRepository repository;
	
	public Author get(UserPrincipal principal) {
		return repository.findById(principal.getId()).orElseThrow();
	}
	
}
