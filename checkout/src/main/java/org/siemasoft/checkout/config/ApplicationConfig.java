package org.siemasoft.checkout.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.siemasoft.checkout.card.Card;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("org.siemasoft.checkout")
public class ApplicationConfig {

    @Value("${http.client.timeout}")
    private int timeout;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.getNetworkConfig()
              .setPort(5900)
              .setPortAutoIncrement(false);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("cards")
                 .setBackupCount(2)
                 .setTimeToLiveSeconds(300);
        config.addMapConfig(mapConfig);
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public IMap<String, Card> cards(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("cards");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        RequestConfig config = RequestConfig.custom()
                                            .setConnectTimeout(timeout)
                                            .setConnectionRequestTimeout(timeout)
                                            .setSocketTimeout(timeout)
                                            .build();
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }
}