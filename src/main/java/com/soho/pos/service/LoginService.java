package com.soho.pos.service;

import com.soho.pos.dao.ManagerDAO;
import com.soho.pos.dao.MenuGroupDAO;
import com.soho.pos.dto.LoginDTO;
import com.soho.pos.entity.Manager;
import com.soho.pos.entity.Menu;
import com.soho.pos.entity.MenuGroup;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {
    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private MenuGroupDAO menuGroupDAO;
    
    public void loginVerify(LoginDTO rq, HttpSession session, HttpServletRequest request) throws Exception {
        
        //帳號是否存在
        Manager manager = managerDAO.findByAccountAndPassword(rq.getAccount(), rq.getPassword());
        if (manager == null) {
            throw new PortalException(ErrorText.LOGIN_FAIL.getMsg());
        }
        
        //停用
        if ("0".equals(manager.getStatus())) {
            throw new PortalException(ErrorText.LOGIN_STATUS_FAIL.getMsg());
        }
        List<MenuGroup> menuGroupList = menuGroupDAO.findAll().stream().sorted(Comparator.comparing(MenuGroup::getSeq)).collect(Collectors.toList());
        for (MenuGroup menuGroup : menuGroupList) {
            List<Menu> menuList = menuGroup.getMenuList().stream().sorted(Comparator.comparing(Menu::getSeq)).collect(Collectors.toList());
            menuGroup.setMenuList(menuList);
        }
        session.invalidate();
        session = request.getSession(true);
        session.setAttribute("manager", manager);
        session.setAttribute("menuGroupList", menuGroupList);
    }
    
    public void logoutVerify(HttpSession session, HttpServletRequest request) throws Exception {
        session.removeAttribute("manager");
        session.removeAttribute("pageMap");
        session.invalidate();
        Cookie cookie = request.getCookies()[0];
        cookie.setMaxAge(0);
    }
    
}
