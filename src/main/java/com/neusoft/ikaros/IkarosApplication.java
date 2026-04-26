package com.neusoft.ikaros;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neusoft.ikaros.mapper")
public class IkarosApplication {

    public static void main(String[] args) {
        SpringApplication.run(IkarosApplication.class, args);
    }

}
