package com.gpex.scheduler;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.gpex")
public class SchedulerApplication {

    public static void main(String[] args) {
        // WebApplication 미사용 데몬프로세스
        new SpringApplicationBuilder(SchedulerApplication.class).web(WebApplicationType.NONE).run(args);
    }
}
