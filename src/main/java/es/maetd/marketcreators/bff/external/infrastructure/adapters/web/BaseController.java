package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.exception.ForbiddenException;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.Role;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContext;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContextHolder;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {

    public void doValidateCsbPermission(String csbFromRequest) {

        UserTokenContext context = UserTokenContextHolder.get();

        if (!hasRole(context, Role.EVALUATION_ACCESS)) {
            log.warn("User {} does not have evaluation_access role", context.getUsername());
            throw new ForbiddenException("This action is forbidden");
        }

        String userCsb = getUserCsb(context).orElse(null);

        if (!hasRole(context, Role.IS_ADMIN) && (csbFromRequest == null || !csbFromRequest.equals(userCsb))) {
            log.warn(
                    "Forbidden CSB access: user={}, requestedCsb={}, userCsb={}",
                    context.getUsername(),
                    csbFromRequest,
                    userCsb);
            throw new ForbiddenException("This action is forbidden");
        }
    }

    private boolean hasRole(UserTokenContext context, Role role) {
        return context.getRoles() != null && context.getRoles().contains(role.getValue());
    }

    private Optional<String> getUserCsb(UserTokenContext context) {
        return Optional.ofNullable(context.getClaims())
                .map(claims -> claims.get("csb"))
                .filter(claim -> !claim.isNull())
                .map(com.auth0.jwt.interfaces.Claim::asString);
    }
}
