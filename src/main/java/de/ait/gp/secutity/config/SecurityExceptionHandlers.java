package de.ait.gp.secutity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.gp.dto.StandardResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletResponse;


public class SecurityExceptionHandlers {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final AuthenticationEntryPoint AUTHENTICATION_ENTRY_POINT = (request, response, authException) ->
            fillResponse(response, HttpStatus.UNAUTHORIZED,"User unauthorized");

    public static final AccessDeniedHandler ACCESS_DENIED_HANDLER = (request, response, accessDeniedException) -> {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        fillResponse(response,HttpStatus.FORBIDDEN,"Access denied for user with email <" +
                authentication.getName() + "> and role " + authentication.getAuthorities());
    };

    public static final AuthenticationSuccessHandler AUTHENTICATION_SUCCESS_HANDLER = (request, response, authentication) ->
            fillResponse(response,HttpStatus.OK, "Login successful");

    public static final AuthenticationFailureHandler AUTHENTICATION_FAILURE_HANDLER = (request, response, exception) ->
            fillResponse(response,HttpStatus.UNAUTHORIZED, "Incorrect password or email");

    public static final LogoutSuccessHandler LOGOUT_SUCCESS_HANDLER = (request, response, authentication) ->
            fillResponse(response,HttpStatus.OK,"Logout successful");




    private static void fillResponse(HttpServletResponse response, HttpStatus status, String message) {
        try {
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            StandardResponseDto responseDto = StandardResponseDto.builder()
                    .message(message)
                    .build();

            String body = objectMapper.writeValueAsString(responseDto);

            response.getWriter().write(body);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
