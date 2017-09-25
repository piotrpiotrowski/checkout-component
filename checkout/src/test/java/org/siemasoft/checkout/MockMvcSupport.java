package org.siemasoft.checkout;

import com.hazelcast.core.HazelcastInstance;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemasoft.checkout.controller.OrderController;
import org.siemasoft.checkout.error.GlobalServiceExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CheckoutApplication.class)
@AutoConfigureStubRunner(ids = {"org.siemasoft:customer:0.0.1-SNAPSHOT:stubs:9910", "org.siemasoft:pricing:0.0.1-SNAPSHOT:stubs:9920"}, workOffline = true)
public class MockMvcSupport {

    @Autowired
    OrderController orderController;

    @Autowired
    GlobalServiceExceptionHandler globalServiceExceptionHandler;

    @Autowired
    @Qualifier("hazelcastInstance")
    HazelcastInstance hazelcastInstance;

    @Before
    public void setup() {
        StandaloneMockMvcBuilder mockMvcBuilder = MockMvcBuilders.standaloneSetup(orderController)
                                                                 .setControllerAdvice(globalServiceExceptionHandler);
        RestAssuredMockMvc.standaloneSetup(mockMvcBuilder);
    }

    @Test
    public void shouldLoadContext() throws Exception {
    }
}
