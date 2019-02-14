package com.jnu.wenda.controller;

import com.jnu.wenda.model.HostHolder;
import com.jnu.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Joe
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;


    //登录注册界面
    @RequestMapping("/reglogin")
    public String reglogin(Model model, @RequestParam(value = "next", required = false) String next) {
        if(next != null)
        model.addAttribute("next", next);
        return "login";
    }

    //注册
    @PostMapping(value = "/reg/")
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse response) {

        try {
            Map<String, String> map = userService.register(username, password);
            //包含msg说明注册失败
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
            }


            return "redirect:/";
        } catch (Exception e) {
            logger.error("注册异常");
            return "login";
        }

    }




    @PostMapping(value = "/login/")
    public String login(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next",required = false) String next,
                      @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                      HttpServletResponse response) {

        try {
            Map<String, String> map = userService.login(username, password);
            //包含msg说明登录验证失败
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            if (!"".equals(next)) {
                return "redirect:" + next;
            }
            return "redirect:/";
        } catch (Exception e) {
            logger.error("登录异常");
            return "login";
        }

    }

    //退出
    @GetMapping(value = "/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        hostHolder.clear();
        return "redirect:/";
    }


}
