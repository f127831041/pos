package com.soho.pos.interceptor;

import com.soho.pos.entity.Manager;
import com.soho.pos.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    
    /**
     * preHandle()：請求送到Controller前執行，回傳一個布林值，如果是true通過攔截器，反之則否。
     * postHandle()：Controller處理完後執行。
     * afterCompletion()：整個請求及回應結束後執行。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 取得user信息，如果沒有直接返回
            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession(true);
            }
            Manager manager = (Manager) session.getAttribute("manager");
            String preURI = request.getRequestURI().split("/")[2];
            boolean isAjax = !StringUtils.isBlank(request.getHeader("X-Requested-With")) && "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
            // ajax驗證
            if (isAjax && ObjectUtils.isEmpty(manager)) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }
            if (request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT")) {
                Object requestBody = request.getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT");
                if (requestBody instanceof String) {
                    String trimmedRequestBody = ((String) requestBody).trim();
                    request.setAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", trimmedRequestBody);
                }
            }
            // 頁面驗證
            if (ObjectUtils.isEmpty(manager) && "login".equals(preURI)) {
                return true;
            } else if (!ObjectUtils.isEmpty(manager) && "login".equals(preURI)) {
                response.sendRedirect(request.getContextPath() + "/home");
                return false;
            } else if (!ObjectUtils.isEmpty(manager) && !"login".equals(preURI)) {
                return true;
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        } catch (Exception ex) {
            ExceptionUtils.toString(ex);
        }
        return false;
    }
}
