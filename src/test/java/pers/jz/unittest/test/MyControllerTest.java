package pers.jz.unittest.test;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pers.jz.unittest.controller.MathController;
import pers.jz.unittest.controller.UserController;
import pers.jz.unittest.entity.User;
import pers.jz.unittest.service.MathService;
import pers.jz.unittest.service.UserService;

import javax.annotation.Resource;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jemmyzhang on 2018/4/3.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class MyControllerTest {

    @Resource
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void testTemplate() throws Exception {
        given(userService.findDefaultUser()).willReturn(new User(1L, "admin", "admin@163.com", "Internet business road 499"));
        User defaultUser = userService.findDefaultUser();
        mvc.perform(get("/users/default").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(new Gson().toJson(defaultUser)));
    }
}
