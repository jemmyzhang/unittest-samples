package pers.jz.unittest.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
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
public class ExampleRestClientTest {

    @Resource(name = "restService")
    private RestService restService;

    @Resource
    private MockRestServiceServer mockRestServiceServer;

    @Test
    public void testRestResult() throws Exception {
        mockRestServiceServer.expect(requestTo("/")).andRespond(withSuccess("root", MediaType.TEXT_PLAIN));
        String call = restService.invokeRoot();
        assertThat(call).isEqualTo("root");
    }

}
