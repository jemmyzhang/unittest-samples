package pers.jz.unittest.service;

import org.springframework.stereotype.Service;

/**
 * @author jemmyzhang on 2018/4/3.
 */
@Service
public class MathService {

    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    public Integer divide(Integer x, Integer y) {
        return x / y;
    }
}
