package com.zyun.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ZYunGatewayApplication
 * @Author: zsp
 * @Date 2021/3/21 20:25
 * @Description:
 * @Version 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.zyun.gateway", "com.zyun.framework.utils"})
@EnableZuulProxy//此工程是一个zuul网关
public class ZYunGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZYunGatewayApplication.class);
    }

}
