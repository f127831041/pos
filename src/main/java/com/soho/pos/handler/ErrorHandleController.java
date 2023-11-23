package com.soho.pos.handler;

import com.soho.pos.enums.PageView;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author viper
 * @Date 2022/12/19 下午 04:47
 * @Version 1.0
 */
@Controller
public class ErrorHandleController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return PageView.ERROR.getPath();
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return PageView.ERROR.getPath();
            }
        }
        return PageView.ERROR.getPath();
    }
}
