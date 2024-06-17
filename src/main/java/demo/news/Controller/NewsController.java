package demo.news.Controller;

import demo.news.Dto.NewsDto;
import demo.news.Model.Category;
import demo.news.Model.News;
import demo.news.Repository.CategoryRepository;
import demo.news.Repository.NewsRepository;
import demo.news.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
	
	private final NewsRepository newsRepository;
	private final CategoryRepository categoryRepository;
	private final AuthorController authorController;
	
	List<String> categoryNames() {
		return categoryRepository.findAll().parallelStream().map(Category::getName).toList();
	}
	
	News get(Long id) {
		return newsRepository.findById(id).orElseThrow();
	}
	
	News get(Long newsId, UserPrincipal principal) {
		val news = get(newsId);
		
		if (news.getAuthor().getId().equals(principal.getId())) {
			return news;
		} else {
			throw new RuntimeException("Aborted edit");
		}
	}
	
	@GetMapping
	String index(Model model) {
		model.addAttribute("news", newsRepository.findAll());
		model.addAttribute("categories", categoryNames());
		return "news";
	}
	
	@GetMapping("/{category}")
	String byCategory(@PathVariable String category, Model model) {
		model.addAttribute("news", newsRepository.findAllByCategoryName(category));
		model.addAttribute("categories", categoryNames());
		return "news";
	}
	
	@GetMapping("/show/{id}")
	String show(@PathVariable Long id, Model model) {
		model.addAttribute("newsItem", get(id));
		model.addAttribute("categories", categoryNames());
		return "show-news";
	}
	
	@GetMapping("/destroy/{id}")
	String destroy(@PathVariable Long id) {
		newsRepository.deleteById(id);
		return "redirect:/news";
	}
	
	@GetMapping("/create")
	String createPage(Model model) {
		model.addAttribute("news", new NewsDto());
		model.addAttribute("categories", categoryNames());
		return "edit-news";
	}
	
	@GetMapping("/edit/{id}")
	String editPage(@PathVariable Long id, Model model, @AuthenticationPrincipal UserPrincipal principal) {
		model.addAttribute("news", NewsDto.from(get(id, principal)));
		model.addAttribute("categories", categoryNames());
		return "edit-news";
	}
	
	@GetMapping("/my")
	String myNews(Model model, @AuthenticationPrincipal UserPrincipal principal) {
		model.addAttribute("news", newsRepository.findAllByAuthorId(principal.getId()));
		model.addAttribute("categories", categoryNames());
		return "my-news";
	}
	
	@PostMapping("/store")
	String store(
			@Valid @ModelAttribute("news") NewsDto d,
			Errors errors,
			@AuthenticationPrincipal UserPrincipal principal
	) {
		if (errors.hasErrors()) {
			return "edit-news";
		}
		
		val news = d.getId() == null ? new News() : get(d.getId(), principal);
		val category = categoryRepository
				.findByName(d.getCategory().toLowerCase())
				.orElseGet(() -> new Category(d.getCategory()));
		
		news.setTitle(d.getTitle());
		news.setContent(d.getContent());
		news.setCategory(categoryRepository.save(category));
		news.setAuthor(authorController.get(principal));
		
		return "redirect:/news/" + newsRepository.save(news).getCategory().getName();
	}
	
}
