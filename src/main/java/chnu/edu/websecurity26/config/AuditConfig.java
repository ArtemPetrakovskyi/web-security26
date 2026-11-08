package chnu.edu.websecurity26.config;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  AuditConfig
  @version  1.0.0
  @since 08/11/2026 - 16.05
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
