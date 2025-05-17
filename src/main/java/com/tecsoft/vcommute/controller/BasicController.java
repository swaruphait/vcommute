package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tecsoft.vcommute.service.CommuteService;

@Controller
public class BasicController {


    @Autowired
    private CommuteService commuteService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/updateDataBygoogleapi", method = RequestMethod.GET)
    public String apiupdate(HttpServletRequest request, HttpServletResponse
    response) {
    commuteService.updateDataBygoogleapi().toString();
    String referer = request.getHeader("Referer");
    return "redirect:" + referer;
    }
}
