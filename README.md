# Spring Boot单元测试指南 #
单元测试是编写测试代码，用来检测特定的、明确的、细颗粒的功能。Spring Boot提供了多种工具包和注解来帮助用户对应用进行单元测试。Spring Boot测试支持由下面两个模块提供：`spring-boot-test`包含了核心组件，而`spring-boot-test-autoconfigure`则能够提供自动化的配置。  

一般情况下，开发者可以在POM通过添加`spring-boot-starter-test`来支持单元测试。  

## 基础概念
单元测试并不一定保证程序功能是正确的，更不保证整体业务是准备的。单元测试不仅仅用来保证当前代码的正确性，更重要的是用来保证代码修复、改进或重构之后的正确性。一般来说，单元测试任务包括：
1. 接口功能测试：用来保证接口功能的正确性。
2. 局部数据结构测试（不常用）：用来保证接口中的数据结构是正确的
   - 比如变量有无初始值	
   - 变量是否溢出
3. 边界条件测试
   - 变量没有赋值（即为NULL）
   - 变量是数值（或字符)
     - 主要边界：最小值，最大值，无穷大（对于DOUBLE等）
     - 溢出边界（期望异常或拒绝服务）：最小值-1，最大值+1
     - 临近边界：最小值+1，最大值-1	
   - 变量是字符串
     - 引用“字符变量”的边界
     - 空字符串
     - 对字符串长度应用“数值变量”的边界
   - 变量是集合
     - 空集合
     - 对集合的大小应用“数值变量”的边界
     - 调整次序：升序、降序
   - 变量有规律
     - 比如对于Math.sqrt，给出n^2-1，和n^2+1的边界
4. 所有独立执行通路测试：保证每一条代码，每个分支都经过测试
   - 代码覆盖率
     - 语句覆盖：保证每一个语句都执行到了
     - 判定覆盖（分支覆盖）：保证每一个分支都执行到
     - 条件覆盖：保证每一个条件都覆盖到true和false（即if、while中的条件语句）
     - 路径覆盖：保证每一个路径都覆盖到
   - 相关软件
     - Cobertura：语句覆盖
     - Emma: Eclipse插件Eclemma
5. 各条错误处理通路测试：保证每一个异常都经过测试

## 测试Spring Boot的应用 ##
Spring Boot应用包含了Spring的应用上下文。如果您在测试的时候需要使用到Spring Boot的特性或者Spring的应用上下文，Spring Boot提供了一种`@SpringBootTest`注解，可以帮助您运行并测试Spring应用。  

您还可以通过`@SpringBootTest`注解的`webEnvironment`属性来改变运行的属性。  

* `MOCK`：该模式应用会装载`WebApplicationContext`并提供一个虚拟的Servlet环境。内置的Servlet容器不会被启动。如果Classpath中没有Servlet API，该模式会启动一个非WEB的普通`ApplicationContext`。
* `RANDOM_PORT`：应用装载了`ServletWebServerApplicationContext`提供了一个真实的Servlet容器环境，不过该容器监听的端口是随机的。
* `DEFINED_PORT`：和`RANDOM_PORT`模式类似，区别在于端口号是固定的，该端口号会从`application.properties`中读取，或者默认`8080`。
* `NONE`：仅仅装载了`ApplicationContext`，不会提供任何Servlet环境（例如mock或者其他）

**注意：**

> * 除了`@SpringBootTest`还有很多其他的注解，为单元测试提供更多个性化的测试。  
> * 不要忘记添加`@RunWith(SpringRunner.class)`注解，不然其他注解将会被忽略。  

下例是测试一个可运行的Server应用。

```java
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


```
在上面这个例子中(1)和(2)分别添加了`@RunWith(SpringRunner.class)`注解和`@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`注解，这样就能启动Spring上下文。`@Resource`能够将Spring容器中的实例注入到代码中；在方法上加入`@Test`注解使方法成为一个测试用例。在用例中，我们可以使用`Assert`断言来判断函数的执行结果是否符合我们的逻辑预期。

### Mock Bean和Spy Bean
上文的例子中包含了`@MockBean`注解和`@SpyBean`注解（注释中）。所谓的Mock测试，指的是在测试过程中，对于某些不容易构造或者不容易获取的对象，用一个虚拟的对象来创建以便测试的测试方法。Mock测试同时可以对某些现实环境中难以触发的失败情形进行测试。  

`@MockBean`注解能够定义一个bean为虚拟形式，该注解能够直接作用于类对属性或者配置类。Mock bean会在每一个`@Test`测试方法执行以后被自动重置。在上面的例子中，我们虚拟了一个`UserService`的一个方法，使之能够返回一个虚拟的`User`对象，并为`User`对象赋予特定的属性值。这样在服务被调用时，返回的对象就是我们预先定义好的返回值。

```java
BDDMockito.given(mockUserService.findDefaultUser()).willReturn(new User(-1L, "ADMIN", "ADMIN_EMAIL", "ADMIN_ADDR"));
Object result = mockUserService.findDefaultUser();
```
对于`@SpyBean`，和`@MockBean`的区别就在于如果你mock了一个类，那么这个类的所有的函数都被Mockito改写了（如果是没有返回值的函数，则什么都不做，如果是有返回值，会返回默认值，比如布尔型的话返回false，List的话会返回一个空的列表，int的话会返回0等等），如果你Spy了一个类，那么所有的函数都没有被改变，除了那些被你打过桩的函数。


## 自动化配置的测试
Spring Boot的自动配置系统对Spring Boot应用是一种非常好的体验，但是对于单元测试而言，似乎有点过犹不及。在单元测试场景，我们经常会遇到如下场景：我们在某一个时刻只需要测试系统的部分功能，但不想牵涉到其他的模块。例如我们希望测试Spring MVC的控制器映射的URL是否正确，而并不想实际调用到数据库。或者你指向测试JPA实体，而并不想触及Web层。Spring Boot引入了一个测试切片（slice）的概念，能够帮助我们限制我们想要测试的组件。注解的命名形式是`@…​Test`，例如`@JsonTest`，`@WebMvcTest`等。每一个切片测试只会引入很有限的自动配置类，您可以使用注解上的`excludeAutoConfiguration`属性来排除部分自动配置类。如果您对切片测试不感兴趣，但是又只想使用部分自动配置类，您可以使用`@AutoConfigure…​`注解和标准的`@SpringBootTest`的组合配置。

所有自动化配置的加载类可以在[https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html](https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html "https://docs.spring.io/spring-boot/docs/current/reference/html/test-auto-configuration.html")里找到。

### 自动化配置的JSON测试
想要测试JSON序列化和反序列化工作是否正常，您可以使用`@JsonTest`注解，`@JsonTest`自动化配置支持以下工具类来支持JSON映射器：

* Jackson `ObjectMapper`
* `Gson`
* `Jsonb`

Spring Boot还提供了基于AssertJ的`JSONassert`和`JsonPath`灯类库来辅助测试。`JacksonTester`，`GsonTester`，`JsonbTester`和`BasicJsonTester`可以分别被应用在Jackson, Gson, Jsonb和字符串。当使用了`@JsonTest`注解，所有这些类库能够使用`@Autowired`进行依赖注入。
下面是JsonTest的一个例子：

```java
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

```

### 自动化配置的Spring MVC测试
`@WebMvcTest`可以用来测试Spring MVC的控制器是否工作正常。自动化配置会加载`@Controller`，`@ControllerAdvice`，`@JsonComponent`，`Converter`，`GenericConverter`，`Filter`，`WebMvcConfigurer`，`HandlerMethodArgumentResolver`等组件，但是上下文中基于`@Component`注解的bean将**不会被扫描到**。  

> Notes: 如果需要加载其他额外的组件，例如刚才说到的基于`@Component`注解的组件，可以使用`@Import`类注解来帮助我们加载需要加载的类。  

一般而言，一个`@WebMvcTest`之对应于单一个Controller，并且利用`@MockBean`进行请求合并。`@WebMvcTest`自动加载了`MockMvc`，因此我们能够快速测试MVC控制器而不需要启动一个完整的HTTP服务器。
下面是一个基于`@WebMvcTest`的例子：

```java
package pers.jz.unittest.test;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pers.jz.unittest.controller.UserController;
import pers.jz.unittest.entity.User;
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

```

### 自动化配置的REST客户端测试
`@RestClientTest`注解可以用于测试REST客户端，默认情况下，该注解能够自动配置Jackson，GSON和Jsonb。如果想要测试特定的类，则需要使用到`@RestClientTest`类的value或components注解。  
以下是一个REST调用的例子：  
首先定义了一个Service类：

```JAVA
package pers.jz.unittest.service;

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
```

并在WebConfig文件里实例化了一个RestTemplate:

```JAVA
package pers.jz.unittest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author jemmyzhang on 2018/4/3.
 */
@Configuration
public class WebConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```
REST测试类如下：

```JAVA
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
@Import(WebConfig.class)//...(1)
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
        mockServerRestTemplateCustomizer.customize(restTemplate);//...(2)
        mockRestServiceServer.expect(requestTo("http://localhost:8080/")).andRespond(withSuccess("tomcat8", MediaType.TEXT_PLAIN));
        String call = restService.invokeRoot();
        assertThat(call).isEqualTo("tomcat8");
    }

}

```
由于RestService依赖RestTemplate，而`@RestClientTest`不会自动加载带有`@Component`注解的组件，因此需要使用（1）中的@Import注入，并且在（2）中配置到MockServerRestTemplateCustomizer中。mockRestServiceServer能够虚拟请求的一个返回结果，而不会真正调用REST接口。

## 参考文献
> https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html  
> https://blog.csdn.net/dc_726/article/details/8713236   
> https://www.cnblogs.com/AloneSword/p/4109407.html  
