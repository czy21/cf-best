package com.cfbest.server;

import com.cfbest.server.feign.IpApiFeign;
import com.cfbest.server.mapper.TelegramMessageMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackageClasses = IpApiFeign.class)
@MapperScan(basePackageClasses = TelegramMessageMapper.class)
@SpringBootApplication
public class CFBestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CFBestApplication.class, args);
    }
}
