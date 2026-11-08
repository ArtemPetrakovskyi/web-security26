package chnu.edu.websecurity26.model;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  Book
  @version  1.0.0
  @since 08/11/2026 - 15.10
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private String id;
    private String title;
    private String author;
    private Integer year;
}
