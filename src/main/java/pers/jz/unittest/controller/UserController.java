package pers.jz.unittest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.jz.unittest.entity.User;
import pers.jz.unittest.service.UserService;

import javax.annotation.Resource;

/**
 * @author jemmyzhang on 2018/4/3.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/default")
    public User findDefault() {
        return userService.findDefaultUser();
    }
}
