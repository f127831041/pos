
package com.soho.pos.controller;

import com.soho.pos.dto.MemberDTO;
import com.soho.pos.entity.Member;
import com.soho.pos.enums.PageView;
import com.soho.pos.model.PageResult;
import com.soho.pos.model.Result;
import com.soho.pos.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Author viper
 * @Date 2023/4/28 上午 09:29
 * @Version 1.0
 */
@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;
    
    @GetMapping("/member")
    public String init() {
        return PageView.MEMBER.getPath();
    }
    
    @PostMapping("/member/query")
    public ResponseEntity<?> query(@RequestBody MemberDTO rq) {
        PageResult pageResult = memberService.findPageQuery(rq);
        return new ResponseEntity<>(Result.success(pageResult), HttpStatus.OK);
    }
    
    @GetMapping("/member/get/{id}")
    public String get(@PathVariable("id") Long id, Model model) {
        Member vo = memberService.get(id);
        model.addAttribute("vo", vo);
        return PageView.MEMBER.getPath() + "::member";
    }
    
    @GetMapping("/member/phone/{phone}")
    public ResponseEntity<?> get(@PathVariable("phone") String phone, HttpSession session) {
        Member member = memberService.findByPhone(phone);
        session.setAttribute("member", member);
        return new ResponseEntity<>(Result.success(member), HttpStatus.OK);
    }
    
    @GetMapping("/member/phone/clean")
    public ResponseEntity<?> clean(HttpSession session) {
        session.removeAttribute("member");
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/member/add")
    public ResponseEntity<?> add(@RequestBody MemberDTO rq) {
        memberService.add(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @PostMapping("/member/update")
    public ResponseEntity<?> update(@RequestBody MemberDTO rq) {
        memberService.upd(rq);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
    
    @DeleteMapping("/member/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
