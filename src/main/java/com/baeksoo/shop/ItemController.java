package com.baeksoo.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {


    private final ItemRepository itemRepositroy;

    @GetMapping("/list")
    String list(Model model){
       List<Item> result = itemRepositroy.findAll();
        model.addAttribute("items",result);

        return "list.html";
    }

    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    @PostMapping("/add")
    String addPost(String title,Integer price){
        Item item= new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepositroy.save(item);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Integer id ,Model model){
        Optional<Item> result =  itemRepositroy.findById(id.longValue());
         result.ifPresent(item -> model.addAttribute("item",item));
        if(result.isPresent()){
            System.out.println(result.get());
        }
        System.out.println(id);
        return "detail.html";
    }
}
