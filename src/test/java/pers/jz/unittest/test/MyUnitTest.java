package pers.jz.unittest.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pers.jz.unittest.entity.User;
import pers.jz.unittest.service.MathService;
import pers.jz.unittest.service.UserService;

import javax.annotation.Resource;

/**
 * @author jemmyzhang on 2018/4/3.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MyUnitTest {

    @Resource
    private TestRestTemplate testRestTemplate;


    @SpyBean
    private MathService spyMathService;

    @MockBean
    private UserService mockUserService;

    @Test
    public void exampleTest() {
        String body = testRestTemplate.getForObject("/math/", String.class);
        Assert.assertEquals("PI: 3.141592653589793", body);
    }

    @Test
    public void spyMathTest() {
        Object result = spyMathService.add(1, 2);
        Assert.assertEquals(3, result);
    }

    @Test
    public void mockUserTest() {
        BDDMockito.given(mockUserService.findDefaultUser()).willReturn(new User(-1L, "ADMIN", "ADMIN_EMAIL", "ADMIN_ADDR"));
        Object result = mockUserService.findDefaultUser();
        Assert.assertEquals(new User(-1L, "ADMIN", "ADMIN_EMAIL", "ADMIN_ADDR"), result);
    }
}
