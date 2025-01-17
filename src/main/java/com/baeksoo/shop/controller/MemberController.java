package com.baeksoo.shop.controller;

import com.baeksoo.shop.dto.MemberDTO;
import com.baeksoo.shop.entity.Member;
import com.baeksoo.shop.jwt.JwtUtil;
import com.baeksoo.shop.repository.MemberRepository;
import com.baeksoo.shop.service.CustomUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
    @PostMapping("/login/jwt")
    @ResponseBody
    public String loginJWT(@RequestBody Map<String, String> data, HttpServletResponse response) {
        var authToken = new UsernamePasswordAuthenticationToken(data.get("username"), data.get("password"));
     var auth =   authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

       var jwt= JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
       var cookie= new Cookie("jwt",jwt);
       cookie.setMaxAge(10);
       cookie.setPath("/");
       response.addCookie(cookie);
        return jwt;
    }
    @GetMapping("/my-page/jwt")
    @ResponseBody
    String myPageJWT(Authentication auth){
        var user=(CustomUser)auth.getPrincipal();
        System.out.println(user);
        System.out.println(user.getUsername());
        System.out.println(user.getAuthorities());



        return "마이페이지 데이터";
    }




}
