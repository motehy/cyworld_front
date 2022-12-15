package com.cyworld.front.config;

import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class MyDeviceDeleagtingViewResolver extends LiteDeviceDelegatingViewResolver{

	public MyDeviceDeleagtingViewResolver(ViewResolver delegate) {
		super(delegate);
	}

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		ViewResolver delegate = getViewResolver();		
		View view = delegate.resolveViewName(viewName, locale);
		return view;
	}
}
