package com.cyworld.front.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.Optional;


@Slf4j
@Component
public class FrontInterceptor extends HandlerInterceptorAdapter{

	@Autowired(required = false)

	@Value("${spring.profiles:}") private String activeServer;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);

		String requestURI = request.getRequestURI().toLowerCase();
		String refererURL = request.getHeader("REFERER");
		String _isocd = null;
		String _checkApp = null;
		request.setAttribute("_static_domain", "http://localhost:"+request.getLocalPort());
		/*ST : 파라미터 초기화*/
		request.setAttribute("_inter_refererURL", "");

    	return super.preHandle(request, response, handler);
	}
	
}
