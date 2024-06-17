package demo.news.Dto;

import demo.news.Model.News;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewsDto {
	private Long id;
	
	@NotBlank
	private String title;
	
	@NotBlank
	@Size(min = 10)
	private String content;
	
	@NotBlank
	@NotNull
	@Size(min = 2, max = 50)
	private String category;
	
	public static NewsDto from(News n) {
		return new NewsDto(n.getId(), n.getTitle(), n.getContent(), n.getCategory().getName());
	}
}
