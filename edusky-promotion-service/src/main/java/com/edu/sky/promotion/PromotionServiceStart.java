package com.edu.sky.promotion;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
@SpringBootApplication
public class PromotionServiceStart {
    private static Logger logger = LoggerFactory.getLogger(PromotionServiceStart.class);
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(PromotionServiceStart.class)
                .web(false).run(args);

//        SpringApplication.run(PromotionServiceStart.class,args);
        logger.info("============= Edusky-Promotion-Service Start ON SpringBoot Success =============");
    }
}
