package com.cyworld.front.config;

import com.cyworld.front.support.FrontInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mobile.device.switcher.SiteSwitcherHandlerInterceptor;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;
import java.util.List;

/**
 *
* <pre>
* 간략 : .
* 상세 : .
* com.coway.core.config
*   |_ WebConfig.java
* </pre>
*
* @Company : DGIST
* @Author  : junghee
* @Date    : 2018. 10. 12. 오전 10:32:32
* @Version : 1.0
 */
@Slf4j
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
public class WebConfig extends WebMvcConfigurerAdapter { //WebMvcConfigurationSupport

  	@Value("${web.security.ignore.paths:}")      	private String[] ignorePaths; //spring security 제외 경로
  	@Value("${web.redirect.ignore.paths:}")      	private String[] redirectIgPaths; //redirect 제외 경로  	(mobile 변환 제외)
  	@Value("${site.info.cookie.secure.flag:true}")    private Boolean cookieSecureFl; //쿠키 secure처리

    @Value("${server.domain.name:coway.com}")		private String domainName;
    @Value("${server.servlet.session.cookie.name:SESSION}") 		private String sessionId;
    @Value("${server.servlet.session.cookie.secure:true}") 		private boolean sessionSecure;
  	@Value("${server.servlet.session.cookie.max-age:-1}")   private Integer sessionCookieMaxAge;
  	@Value("${spring.profiles:}")      	private String activeServer;

	/*
	 * CORS 필터 (cross domain)
	 * 모든 클라이언트 접근 허용
	 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//        .allowedOrigins("*")
//        .allowedMethods(HttpMethod.GET.name(),HttpMethod.POST.name(),HttpMethod.OPTIONS.name())
        .allowedHeaders("Access-Control-Allow-Origin", "*")
        .allowedHeaders("Access-Control-Allow-Methods", "GET, POST")
        .allowedHeaders("Access-Control-Max-Age", "3600") ;
//        .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Content-type")
//        .allowCredentials(true);
    }

  	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
  	      "classpath:/META-INF/resources/",
  	      "classpath:/resources/",
  	      "classpath:/static/",
  	      "classpath:/META-INF/resources/webjars/",
  	      "/resources/" };

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/**")) {
		      registry.addResourceHandler("/**").addResourceLocations(
		          CLASSPATH_RESOURCE_LOCATIONS);
		}
		registry.addResourceHandler("/i18n/**").addResourceLocations("classpath:/i18n/");
    }

	//view - jsp 설정
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	//view - jsp 설정
//	@Override
//	public void configureViewResolvers(final ViewResolverRegistry registry) {
//	    registry.jsp("/WEB-INF/views/", ".jsp");
//	}

	//@Pathvalue에 값이 있는경우 파라미터보다 우선으로 처리 - WebMvcConfigurationSupport  사용시 사용가능
//    @Override
//    protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
//        return new CustomRequestMappingHandlerAdapter();
//    }

	//mobile url 분리 안할 경우
	@Bean
	public LiteDeviceDelegatingViewResolver liteDeviceAwareViewResolver() {
	    InternalResourceViewResolver delegate =
	            new InternalResourceViewResolver();
	    delegate.setPrefix("/WEB-INF/views/");
	    delegate.setSuffix(".jsp");

	    //divice별 jsp 처리
	    LiteDeviceDelegatingViewResolver resolver =
	            new MyDeviceDeleagtingViewResolver(delegate);
	    resolver.setOrder(0);
	    //divice별 jsp 페이지가 분리되지 않은경우 - resolveViewName 이 null 이면 오리지널 페이지 노출
	    resolver.setEnableFallback(true);
	    resolver.setMobilePrefix("mobile/");
	    resolver.setTabletPrefix("mobile/");

	    return resolver;
	}

	@Bean
	public FrontInterceptor frontIntercceptor() { //FrontInterceptor @Autowired 사용하기 위해 bean 으로 추가
		return new FrontInterceptor();
	}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(frontIntercceptor())
				.addPathPatterns("/**")
				.excludePathPatterns(ignorePaths);
    }

    /**
     * <scheme>://<user>:<password>@<host>:<port>/<path>;<params>?<query>#<fragment> 구조 사용하기 위함
     * ;version=2 등으로 입력된 경우 변환
     **/
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        urlPathHelper.setUrlDecode(false); // 더블 // 허용시
        configurer.setUrlPathHelper(urlPathHelper);
    }

	@Bean(name = "localeResolver") //기본 locale
    public LocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver(); //session은 삭제된 경우 reset 되므로 cookie로 변경
        //localeResolver.setDefaultLocale(StringUtils.parseLocaleString(defaultLangcd)); //defaultLangcd
        localeResolver.setDefaultLocale(StringUtils.parseLocaleString("ko_KR")); //한국어 강제설정
        localeResolver.setCookieHttpOnly(true);
        localeResolver.setCookieSecure(cookieSecureFl);
		localeResolver.setCookieName("language");
        return localeResolver;
    }

	@Bean(name = "messageSource")
    public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:i18n/messages", "classpath:i18n_front/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(-1);
        messageSource.setUseCodeAsDefaultMessage(true); //메세지가 없으면 코드 출력

        return messageSource;
    }

    //device별 분리 후 -> 모바일에서 PC로 보기 등 설정시
    //파라미터 처리
    //Site: <a href="${currentUrl}?site_preference=normal">Normal</a> |
    //<a href="${currentUrl}?site_preference=mobile">Mobile</a>
    @Bean
    public SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor() {
        return new SitePreferenceHandlerInterceptor();
    }

    //device별 페이지 이동 : spring mobile device에서 자동으로 붙여줌
	@Bean
	public SiteSwitcherHandlerInterceptor siteSwitcherHandlerInterceptor() {
		//m.XXX.com 으로 이동시 사용 - 테블릿도 모바일사이트로 이동 = true
		return SiteSwitcherHandlerInterceptor.mDot(domainName, true);

		// /m 으로 이동시 사용
//		return new SiteSwitcherHandlerInterceptor(
//                new NormalSitePathUrlFactory(frontMblUrlPrefix, null),
//                new MobileSitePathUrlFactory(frontMblUrlPrefix, null),
//                new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()),true);
	}

}
