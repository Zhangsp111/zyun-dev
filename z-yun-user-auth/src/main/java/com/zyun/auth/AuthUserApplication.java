package com.zyun.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName AuthUserApplication
 * @Author: zsp
 * @Date 2021/3/16 12:11
 * @Description:
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.zyun.model.auth", "com.zyun.auth"
        ,"com.zyun.api.auth", "com.zyun.framework"})
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class AuthUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthUserApplication.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
