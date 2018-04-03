package pers.jz.unittest.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import pers.jz.unittest.config.WebConfig;
import pers.jz.unittest.service.RestService;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author jemmyzhang on 2018/4/3.
 */
@RunWith(SpringRunner.class)
@RestClientTest(RestService.class)
@Import(WebConfig.class)
public class ExampleRestClientTest {

    @Resource
    private RestTemplate restTemplate;

    @Resource(name = "restService")
    private RestService restService;

    @Resource
    MockServerRestTemplateCustomizer mockServerRestTemplateCustomizer;

    @Resource
    private MockRestServiceServer mockRestServiceServer;

    @Test
    public void testRestResult() throws Exception {
        mockServerRestTemplateCustomizer.customize(restTemplate);
        mockRestServiceServer.expect(requestTo("http://localhost:8080/")).andRespond(withSuccess("tomcat8", MediaType.TEXT_PLAIN));
        String call = restService.invokeRoot();
        assertThat(call).isEqualTo("tomcat8");
    }

}
