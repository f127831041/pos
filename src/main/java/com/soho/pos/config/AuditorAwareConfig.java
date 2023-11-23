package com.soho.pos.config;

import com.soho.pos.entity.Manager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig implements AuditorAware<Long> {
    
    /**
     * 取得目前的登入者id
     * @return
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            // IllegalStateException
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            Object infoObject = requestAttributes.getAttribute("manager", RequestAttributes.SCOPE_SESSION);
            if (infoObject == null) {
                return Optional.empty();
            }
            Manager manager = (Manager) infoObject;
            return Optional.of(manager.getId());
        } catch (IllegalStateException | ClassCastException e) {
            return Optional.empty();
        }
    }
}
