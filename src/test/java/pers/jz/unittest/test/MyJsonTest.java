package pers.jz.unittest.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import pers.jz.unittest.entity.User;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jemmyzhang on 2018/4/3.
 */

@RunWith(SpringRunner.class)
@JsonTest
public class MyJsonTest {

    @Resource
    private JacksonTester<User> json;

    @Test
    public void testSerialize() throws Exception {
        User user = User.withId(1L).name("admin").email("admin@hotmail.com").address("Hangzhou").build();
        assertThat(json.write(user)).isEqualToJson("expected.json");
        assertThat(this.json.write(user)).hasJsonPathStringValue("@.name");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.name").isEqualToIgnoringCase("admin");
    }

    @Test
    public void testDeSerialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"admin\",\"email\":\"admin@hotmail.com\",\"address\":\"Hangzhou\"}";
        User user = new User(1L, "admin", "admin@hotmail.com", "Hangzhou");
        assertThat(json.parse(content)).isEqualTo(user);
        assertThat(json.parseObject(content).getName()).isEqualTo("admin");
    }
}
