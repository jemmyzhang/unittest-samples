package pers.jz.unittest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pers.jz.unittest.service.MathService;

import javax.annotation.Resource;

/**
 * @author jemmyzhang on 2018/4/3.
 */
@RestController
@RequestMapping("/math")
public class MathController {

    @Resource
    MathService mathService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "PI: " + Math.PI;
    }

    @RequestMapping(value = "/add")
    public Integer add(int x, int y) {
        return mathService.add(x, y);
    }

    @RequestMapping(value = "/divide")
    public Integer divide(int x, int y) {
        return mathService.divide(x, y);
    }

}
