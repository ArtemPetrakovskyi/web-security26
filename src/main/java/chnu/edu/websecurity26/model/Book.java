package chnu.edu.websecurity26.model;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  Book
  @version  1.1.0
  @since 08/11/2026 - 15.10
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "books")
public class Book extends Auditable {
    @Id
    private String id;
    private String title;
    private String author;
    private Integer year;
}
