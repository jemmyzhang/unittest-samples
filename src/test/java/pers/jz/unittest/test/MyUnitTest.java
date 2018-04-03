package pers.jz.unittest.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pers.jz.unittest.service.MathService;

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
    //@MockBean
    private MathService spyMathService;

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
    public void mockMathTest() {
        BDDMockito.given(spyMathService.add(1, 2)).willReturn(0);
        Object result = spyMathService.add(1, 2);
        Assert.assertEquals(0, result);
    }


}
