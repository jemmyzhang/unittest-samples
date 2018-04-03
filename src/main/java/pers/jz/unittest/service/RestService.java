package pers.jz.unittest.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author jemmyzhang on 2018/4/3.
 */
@Service
public class RestService {

    @Resource
    RestTemplate restTemplate;

    public String invokeRoot() {
        return restTemplate.getForObject("http://localhost:8080/", String.class);
    }
}
