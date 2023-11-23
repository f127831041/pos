

package com.soho.pos.controller;

import com.soho.pos.dto.ActivityDTO;
import com.soho.pos.dto.MemberDTO;
import com.soho.pos.entity.Activity;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.Result;
import com.soho.pos.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    
    @GetMapping("/activity")
    public String init(Model model) {
        Activity activity = activityService.get(1L);
        model.addAttribute("activity", activity);
        return PageView.ACTIVITY.getPath();
    }
    
    @PostMapping("/activity/update")
    public ResponseEntity<?> update(@RequestBody ActivityDTO rq) {
        activityService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
}
