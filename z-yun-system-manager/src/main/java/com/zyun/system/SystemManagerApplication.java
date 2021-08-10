package com.zyun.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @ClassName UserManagerApplication
 * @Author: zsp
 * @Date 2021/3/16 12:44
 * @Description:
 * @Version 1.0
 */
@ComponentScan(basePackages = {"com.zyun.model.system", "com.zyun.system"
        ,"com.zyun.api.system", "com.zyun.framework"})
@EntityScan(basePackages = "com.zyun.model.system") // 扫描jpa实体类
// 开启权限校验
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
public class SystemManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemManagerApplication.class);

    }

}
