package demo.news.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class News {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String title;
	@Lob
	private String content;
	@Setter(AccessLevel.NONE)
	private LocalDateTime publishDate;
	
	@ManyToOne
	private Category category;
	
	@ManyToOne
	private Author author;
	
	@PrePersist
	@PreUpdate
	private void preUpdate() {
		publishDate = LocalDateTime.now();
	}
}
