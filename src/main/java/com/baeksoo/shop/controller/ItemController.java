package com.baeksoo.shop.controller;

import com.baeksoo.shop.entity.Item;
import com.baeksoo.shop.entity.Member;
import com.baeksoo.shop.repository.ItemRepository;
import com.baeksoo.shop.repository.MemberRepository;
import com.baeksoo.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {


    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MemberRepository memberRepository;

    @GetMapping("/list")
    public String list(Model model) {
        List<Item> res = itemRepository.findAll();

        model.addAttribute("items", res);
        return "list";
    }


    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    @PostMapping("/add")
    String addPost(@ModelAttribute Item item){
        itemService.saveItem(item);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable long id ,Model model){

        Optional<Item> result =  itemService.findById(id);

        if(result.isPresent()){
           model.addAttribute("item",result.get());
           return "detail.html";
        }
        else {
            return "redirect:/list";
        }

    }

    @GetMapping("/edit/{id}")
   public String edit(Model model ,@PathVariable long id){

       Optional<Item> result = itemRepository.findById(id);
       if(result.isPresent()){
           model.addAttribute("item",result.get());
           return "edit.html";
       }else{
           return "redirect:/list";
       }

    }
    @PostMapping("/edit")
    public String editItem( String title,Integer price,Long id){
        Item item = new Item();
        item.setId(id);
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);

        return "redirect:/list";

    }
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") long id) {
        // 아이템 삭제 처리
        itemRepository.deleteById(id);

        // 삭제 후 '/list'로 리다이렉트
        return "redirect:/list";
    }

    @GetMapping("/sinup")
    public String sinup(){

        return "sinup";
    }
    @PostMapping("/sinupadd")
    public String sinupAdd(@ModelAttribute Member member){

        var memberPassword =new BCryptPasswordEncoder().encode(member.getPassword());
        member.setPassword(memberPassword);
        System.out.println(member);

        memberRepository.save(member);
    return "redirect:/list";
    }
}
