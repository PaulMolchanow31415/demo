package demo.news.Controller;

import demo.news.Dto.AuthorDto;
import demo.news.Model.Author;
import demo.news.Repository.AuthorRepository;
import demo.news.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthorRepository repository;
	private final PasswordEncoder encoder;
	
	@GetMapping("/login")
	String showLogin() {
		return "login";
	}
	
	@GetMapping("registration")
	String registration(Model model) {
		model.addAttribute("author", new AuthorDto());
		return "registration";
	}
	
	@PostMapping("/register")
	String register(
			@Valid @ModelAttribute("author") AuthorDto d,
			Errors errors
	) {
		if(errors.hasErrors()) {
			return "registration";
		}
		
		val user = new Author();
		user.setUsername(d.getUsername());
		user.setEmail(d.getEmail());
		user.setPassword(encoder.encode(d.getPassword()));
		user.setRoles(Collections.singleton(Role.EDITOR));
		repository.save(user);
		return "redirect:/login?success";
	}
	
}
