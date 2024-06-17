package demo.news.Dto;

import demo.news.Repository.AuthorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
	
	private final AuthorRepository repository;
	
	@Override
	public boolean isValid(String username, ConstraintValidatorContext ctx) {
		if (repository.count() > 0) {
			return repository.findByUsername(username).isEmpty();
		}
		
		return true;
	}
}
