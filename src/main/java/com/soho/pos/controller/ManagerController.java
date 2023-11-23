
package com.soho.pos.controller;

import com.soho.pos.dto.ManagerDTO;
import com.soho.pos.entity.Manager;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    
    @GetMapping("/manager")
    public String init() {
        return PageView.MANAGER.getPath();
    }
    
    @PostMapping("/manager/query")
    public ResponseEntity<?> query(@RequestBody ManagerDTO rq) {
        PageResult pageResult = managerService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/manager/get/{id}")
    public String get(@PathVariable("id") Long id, Model model) {
        Manager vo = managerService.get(id);
        model.addAttribute("vo", vo);
        return PageView.MANAGER.getPath() + "::manager";
    }
    
    @PostMapping("/manager/add")
    public ResponseEntity<?> add(@RequestBody ManagerDTO rq) {
        managerService.add(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/manager/update")
    public ResponseEntity<?> update(@RequestBody ManagerDTO rq) {
        managerService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @DeleteMapping("/manager/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        managerService.delete(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
