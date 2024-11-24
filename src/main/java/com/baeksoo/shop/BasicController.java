package com.baeksoo.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicController {
    @GetMapping("/")
    String hello(){
        return "indext.html";
    }

    @GetMapping("/about")
    @ResponseBody
    public String about() {
        return "피싱사이트에요";
    }
}
