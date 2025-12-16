package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.interfaces.Claim;
import es.maetd.marketcreators.bff.external.exception.ForbiddenException;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.Role;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.BaseController;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContext;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContextHolder;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BaseControllerTest {

    public static final String CSB = "csb";
    public static final String NUMBER_CSB = "0128";
    private static BaseController controller;

    static class TestBaseController extends BaseController {}

    @BeforeAll
    static void setUp() {

        controller = new TestBaseController();
    }

    @AfterEach
    void cleanup() {
        UserTokenContextHolder.clear();
    }

    @Test
    void shouldThrowForbiddenWhenUserHasNoEvaluationAccessRole() {

        // given
        UserTokenContext testContext = new UserTokenContext("superuser", List.of("access"), null);

        UserTokenContextHolder.set(testContext);

        // Then
        assertThrows(ForbiddenException.class, () -> controller.doValidateCsbPermission("CSB123"));
    }

    @Test
    void shouldAllowWhenAdminRegardlessOfCsb() {

        // given
        Map<String, Claim> claims = Map.of(CSB, mock(Claim.class));
        when(claims.get(CSB).asString()).thenReturn(NUMBER_CSB);
        when(claims.get(CSB).isNull()).thenReturn(false);

        UserTokenContext testContext =
                new UserTokenContext("superuser", List.of(Role.EVALUATION_ACCESS.getValue()), claims);

        UserTokenContextHolder.set(testContext);

        // Then
        assertDoesNotThrow(() -> controller.doValidateCsbPermission(NUMBER_CSB));
    }
}
