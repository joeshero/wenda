package com.jnu.wenda.controller;

import com.jnu.wenda.model.HostHolder;
import com.jnu.wenda.model.UserAndQuestion;
import com.jnu.wenda.service.QuestionService;
import com.jnu.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by Joe
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //主页
    @RequestMapping({"/","/index"})
    public String index(Model model) {

        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }


        List<UserAndQuestion> vos = questionService.getQuestions(0,0,10);
        model.addAttribute("vos", vos);
        model.addAttribute("user", hostHolder.getUser());
        return "index";
    }

    //用户页
    @GetMapping(value = "/user/{userId}")
    public String userIndex(Model model,@PathVariable("userId") int userId) {
        model.addAttribute("vos", questionService.getQuestions(userId, 0, 10));
        return "index";
    }





}
