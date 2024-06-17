package demo.news.Repository;

import demo.news.Model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
	List<News> findAllByCategoryName(String name);
	
	List<News> findAllByAuthorId(Long id);
}
