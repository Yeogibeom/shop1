package com.baeksoo.shop.controller;

import com.baeksoo.shop.entity.Item;
import com.baeksoo.shop.repository.ItemRepository;
import com.baeksoo.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@Controller
@RequiredArgsConstructor
public class ItemController {


    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Item> res = itemRepository.findAll();
        System.out.println("Items: " + res); // 로그로 데이터 출력
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
    @PostMapping("/edit/{id}")
    public String editPost(@ModelAttribute Item item,@PathVariable long id){
        Optional<Item> result = itemRepository.findById(id);
        if(result.isPresent()){
            Item item1 = result.get();
            item1.setTitle(item.getTitle());
            item1.setPrice(item.getPrice());
            itemRepository.save(item1);
        }
        return "redirect:/list";

    }

}
