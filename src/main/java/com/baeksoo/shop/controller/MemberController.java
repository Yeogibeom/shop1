package com.baeksoo.shop.controller;

import com.baeksoo.shop.dto.MemberDTO;
import com.baeksoo.shop.entity.Member;
import com.baeksoo.shop.repository.MemberRepository;
import com.baeksoo.shop.service.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/sinup")
    public String sinup(Authentication auth){
        if (auth.isAuthenticated()){
            return "redirect:/list";
        }
        return "sinup";
    }
    @PostMapping("/member")
    public String sinupAdd(@ModelAttribute Member member){

        var memberPassword =passwordEncoder.encode(member.getPassword());
        member.setPassword(memberPassword);
        System.out.println(member);

        memberRepository.save(member);
        return "redirect:/list";
    }
    @GetMapping("/login")
    public String login(){

        return "login";
    }

    @GetMapping("/fail")
    public String loginFail() {
        return "fail"; // `fail.html` 템플릿 반환
    }
    @GetMapping("/my-page")
    public String myPage(Authentication auth, Model model){
       boolean a = auth.isAuthenticated();
       if(a){
           return "mypage";
       }
       else return "fail";

    }
    @GetMapping("/user/1")
    public MemberDTO getUser(){
        var a =memberRepository.findById(1L);
        var result=a.get();
        var data = new MemberDTO(result.getUsername(),result.getDisplayName());

        return data;


    }
}
