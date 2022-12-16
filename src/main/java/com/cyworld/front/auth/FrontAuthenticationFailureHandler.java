package com.cyworld.front.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
public class FrontAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {    

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {
		String error = request.getParameter("error");
        if(error == null || "".equals(error)) error= ex.getMessage();
		log.debug("===FrontAuthenticationFailureHandler[error] : {} - {}", ex.getMessage(), error);

		request.setAttribute("loginfl","false");
		getRedirectStrategy().sendRedirect(request, response, "/login");
		
	}

}
