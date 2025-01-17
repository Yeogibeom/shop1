package com.baeksoo.shop.controller;

import com.baeksoo.shop.entity.Comment;
import com.baeksoo.shop.entity.Item;
import com.baeksoo.shop.entity.Member;
import com.baeksoo.shop.repository.CommentRepository;
import com.baeksoo.shop.repository.ItemRepository;
import com.baeksoo.shop.repository.MemberRepository;
import com.baeksoo.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final CommentRepository commentRepository;

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

        List<Comment> comments= commentRepository.findAllByParentId(1L);
        log.info(comments.toString());

        model.addAttribute("comments", comments);

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

    @GetMapping("/list/page/{abc}")
    String getListPage(Model model, @PathVariable Integer abc) {
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(abc-1, 5));
        model.addAttribute("items", result);

        return "list";
    }

    @PostMapping("/search")
    public String search(@RequestParam String searchText) {
      var result=  itemRepository.rawQuery1(searchText);
//      행이 많아지면 index를 사용하면됨 pull text index는 단어로 정렬하는것  한국어는 n-gram parser이용 문자를 두글자식 만들어서 정렬
      log.info(result.toString());
        return "list";
    }
}
