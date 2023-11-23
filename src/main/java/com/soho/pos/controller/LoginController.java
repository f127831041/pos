package com.soho.pos.controller;

import com.soho.pos.dto.LoginDTO;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.Result;
import com.soho.pos.service.LoginService;
import com.soho.pos.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author viper
 * @Date 2023/4/27 下午 02:22
 * @Version 1.0
 */
@Controller
public class LoginController {
    
    @Autowired
    private LoginService loginService;
    
    @GetMapping("/")
    public String init() {
        return "redirect:" + SpringUtils.getHandlerPath("/login");
    }
    
    @GetMapping("/login")
    public String login() {
        return PageView.LOGIN.getPath();
    }
    
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginDTO rq, HttpSession session, HttpServletRequest request) throws Exception {
        loginService.loginVerify(rq, session, request);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/doLogOut")
    public ResponseEntity<?> doLogOut(HttpSession session, HttpServletRequest request) throws Exception {
        loginService.logoutVerify(session, request);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
