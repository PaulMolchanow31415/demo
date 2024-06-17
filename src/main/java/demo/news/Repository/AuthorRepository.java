package demo.news.Repository;

import demo.news.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	Optional<Author> findByUsername(String username);
}
