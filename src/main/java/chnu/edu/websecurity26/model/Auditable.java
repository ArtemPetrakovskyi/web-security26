package chnu.edu.websecurity26.model;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  Auditable
  @version  1.0.0
  @since 08/11/2026 - 16.10
*/

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

/**
 * Audit fields. Spring Data MongoDB fills them automatically on every save.
 */
@Getter
@Setter
public abstract class Auditable {

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private Long version;
}
