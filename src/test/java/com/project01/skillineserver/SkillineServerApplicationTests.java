package com.project01.skillineserver;

import com.project01.skillineserver.service.MediaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SkillineServerApplicationTests {

    @Autowired
    MediaService mediaService;

    @Test
    void contextLoads() throws IOException, InterruptedException {
        mediaService.processVideo("9ece8d2b-93fe-4e12-b6cf-9be73e63f974");
    }

}
