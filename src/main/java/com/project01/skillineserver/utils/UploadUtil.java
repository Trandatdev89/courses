package com.project01.skillineserver.utils;

import com.project01.skillineserver.entity.LectureEntity;
import com.project01.skillineserver.enums.FileType;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Component
public class UploadUtil {

    @Value("${upload.directory.video}")
    private String videoPath;

    @Value("${upload.directory.image}")
    private String imagePath;

    @Value("${upload.directory.pdf}")
    private String pdfPath;


    @PostConstruct
    public void init() {
        File fileVideo = new File(videoPath);
        File fileImage = new File(imagePath);
        if(!fileVideo.exists()) {
            fileVideo.mkdirs();
        }
        if(!fileImage.exists()) {
            fileImage.mkdirs();
        }
    }

    public LectureEntity generateVideoUrl(MultipartFile lectureFile, LectureEntity lectureEntity) throws IOException, InterruptedException {

        Path pathVideo = createPathFile(lectureFile, FileType.VIDEO);

        String durationVideo = getVideoDuration(pathVideo.toString());
        String imageVideo = extractThumbnail(pathVideo.toString());

        lectureEntity.setContentType(lectureFile.getContentType());
        lectureEntity.setDuration(durationVideo);
        lectureEntity.setImage(Paths.get(imagePath,imageVideo).toString());
        lectureEntity.setFilePath(Paths.get(videoPath,pathVideo.getFileName().toString()).toString());

        return lectureEntity;
    }

    public String convertVideoUrl(String fileName,String folder){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getScheme() + "://" + request.getServerName()+":"
                + request.getServerPort()+ "/" + folder+ "/" + fileName;
    }

    private String getVideoDuration(String filePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                filePath
        );

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String duration = reader.readLine();
        process.waitFor();
        return duration;
    }

    public String extractThumbnail(String videoPath) throws IOException, InterruptedException {

        String fileName = UUID.randomUUID().toString() + ".jpg";

        Path folderImage = Paths.get(imagePath);

        Path imageFilePath = folderImage.resolve(fileName).normalize().toAbsolutePath();


        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", videoPath,
                "-ss", "00:00:05",
                "-vframes", "1",
                imageFilePath.toString()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Failed to extract thumbnail from video: " + videoPath);
        }

        return fileName;

    }

    private Path createPathFile(MultipartFile lectureFile, FileType fileType) throws IOException {

        String originFileName = lectureFile.getOriginalFilename();
        Path folderUpload = null;

        switch (fileType){
            case VIDEO:
                folderUpload = Paths.get(videoPath);
                break;
            case PDF:
                folderUpload = Paths.get(pdfPath);
                break;
            case IMAGE:
                folderUpload = Paths.get(videoPath);
                break;
            default:
                folderUpload = Paths.get(imagePath);
                break;
        }

        String fileExtension = StringUtils.getFilenameExtension(originFileName);

        String fileName = Objects.isNull(fileExtension)
                ? UUID.randomUUID().toString()
                : UUID.randomUUID().toString() + "." + fileExtension;

        Path filePath = folderUpload.resolve(fileName).normalize().toAbsolutePath();


        Files.copy(lectureFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath;
    }

}
