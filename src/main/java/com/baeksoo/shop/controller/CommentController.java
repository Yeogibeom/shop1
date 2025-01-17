package com.baeksoo.shop.controller;

import com.baeksoo.shop.entity.Comment;
import com.baeksoo.shop.entity.Item;
import com.baeksoo.shop.repository.CommentRepository;
import com.baeksoo.shop.service.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentRepository commentRepository;
    @PostMapping("/comment")
    public String postComment(@RequestParam String comment, Authentication auth ,@RequestParam Long parent) {
        CustomUser principal = (CustomUser) auth.getPrincipal();
        var data = new Comment();
        data.setContent(comment);
        data.setUsername(principal.getUsername());
        data.setParentId(parent);
        commentRepository.save(data);
        return "redirect:/list";
    }



}
