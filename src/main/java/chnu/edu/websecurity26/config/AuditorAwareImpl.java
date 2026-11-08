package chnu.edu.websecurity26.config;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  AuditorAwareImpl
  @version  1.0.0
  @since 08/11/2026 - 16.05
*/

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Returns the name of the user who changes a document.
     * For HTTP requests it is the logged-in user (admin),
     * for the initial data loaded at startup it is "system".
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("system");
        }
        return Optional.of(authentication.getName());
    }
}
