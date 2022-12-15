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
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String intro(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "home";
    }
}
