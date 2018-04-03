package pers.jz.unittest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.jz.unittest.service.RestService;

import javax.annotation.Resource;

/**
 * @author jemmyzhang on 2018/4/3.
 */
@RestController
@RequestMapping("/rest")
public class RestRequestController {

    @Resource
    RestService restService;

    @GetMapping("/root")
    public String invokeRoot() {
        return restService.invokeRoot();
    }
}
