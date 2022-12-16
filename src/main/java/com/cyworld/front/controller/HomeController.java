package com.cyworld.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@Tag(name="front_manage", description="메인")
public class HomeController {

    @Operation(summary = "intro / 메인")
    @RequestMapping(value="/", method = {RequestMethod.GET, RequestMethod.POST})
    public String intro(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "home";
    }

    @Operation(summary = "로그인 페이지")
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.info((String) request.getAttribute("loginfl"));
        return "/login";
    }

    @Operation(summary = "로그인 성공 페이지")
    @RequestMapping(value="/login/success", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "login_success";
    }
}
