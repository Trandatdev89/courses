package com.project01.skillineserver.service;

import java.io.IOException;

public interface FileService {
    void processVideo(String videoId) throws IOException, InterruptedException;
}
