package com.gpex.admin.api;

import com.gpex.common.utils.HttpUtils;
import com.gpex.common.utils.StringUtils;
import com.gpex.database.enums.DatabaseEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@EnableCaching
@SpringBootApplication(scanBasePackages = "com.gpex")
@RestController
public class AdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
    }

    @Autowired
    private Environment env;

    @GetMapping("/")
    public ResponseEntity<?> testPublicPost(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> bodyParam) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("contentType", request.getContentType());
        resultMap.put("requestUrl", HttpUtils.getRequestURL(request));
        resultMap.put("requestMethod", HttpUtils.getMethod(request));
        resultMap.put("remoteIp", HttpUtils.getRemoteIp(request));
        resultMap.put("bodyParam", bodyParam);
        resultMap.put("appName", "app admin application");
        resultMap.put("dataBaseEnums", Arrays.stream(DatabaseEnums.values()).collect(Collectors.toList()));
        resultMap.put("commonStringUtils", StringUtils.getCommon());
        resultMap.put("activeProfile", env.getActiveProfiles());
        resultMap.put("fixedAt", ZonedDateTime.of(
                2023,
                6,
                29,
                16,
                17,
                0,
                0,
                ZoneId.systemDefault()));

        return ResponseEntity.ok(resultMap);
    }

}
