package pers.jz.unittest.service;

import org.springframework.stereotype.Service;
import pers.jz.unittest.entity.User;

/**
 * @author jemmyzhang on 2018/4/3.
 */

@Service
public class UserService {

    public User findDefaultUser() {
        return User.withId(1L).name("admin").email("admin@alibaba-inc.com").address("Internet business road 499").build();
    }
}
