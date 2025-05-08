package com.example.communityserver.utils;

import com.example.communityserver.entity.po.FileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 文件操作工具类 - 提供文件上传、下载、删除等操作
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-29
 **/
@Slf4j
public class FileUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMM");

    /**
     * 上传文件到指定目录
     *
     * @param file    上传的文件对象
     * @param baseDir 基础存储目录
     * @return FileEntity
     * @throws IOException              文件操作异常
     * @throws IllegalArgumentException 参数不合法异常
     */
    public static FileEntity upload(MultipartFile file, String baseDir) throws IOException {
        // 参数校验
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (baseDir == null || baseDir.trim().isEmpty()) {
            throw new IllegalArgumentException("存储路径不能为空");
        }

        // 处理相对路径
        Path basePath = Paths.get(baseDir).toAbsolutePath().normalize();

        // 创建日期格式的子目录
        String dateDir = DATE_FORMAT.format(new Date());
        // 安全拼接,避免路径遍历攻击,非字符串拼接
        Path uploadPath = basePath.resolve(dateDir);

        // 创建目录（如果不存在）
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = generateUniqueFilename(fileExtension);

        // 存储文件
        Path targetPath = uploadPath.resolve(uniqueFilename);
        file.transferTo(targetPath);

        // 构建返回实体
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileOriginalName(originalFilename);
        fileEntity.setStorageUrl(targetPath.toString());
        fileEntity.setAccessUrl("/uploads" + "/" + dateDir + "/" + uniqueFilename);
        fileEntity.setFileAutoName(uniqueFilename);
        fileEntity.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return fileEntity;
    }

    /**
     * 使用字节流保存文件到指定目录
     *
     * @param file    上传的文件对象
     * @param baseDir 基础存储目录
     * @return FileEntity
     * @throws IOException              文件操作异常
     * @throws IllegalArgumentException 参数不合法异常
     */
    public static FileEntity uploadWithByteStream(MultipartFile file, String baseDir) throws IOException {
        // 参数校验（与原有方法保持一致）
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (baseDir == null || baseDir.trim().isEmpty()) {
            throw new IllegalArgumentException("存储路径不能为空");
        }

        // 处理相对路径（与原有方法保持一致）
        Path basePath = Paths.get(baseDir).toAbsolutePath().normalize();

        // 创建日期格式的子目录（与原有方法保持一致）
        String dateDir = DATE_FORMAT.format(new Date());
        Path uploadPath = basePath.resolve(dateDir);

        // 创建目录（如果不存在）（与原有方法保持一致）
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名（与原有方法保持一致）
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = generateUniqueFilename(fileExtension);

        // 存储文件 - 使用字节流方式（新增部分）
        Path targetPath = uploadPath.resolve(uniqueFilename);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 构建返回实体（与原有方法保持一致）
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileOriginalName(originalFilename);
        fileEntity.setStorageUrl(targetPath.toString());
        fileEntity.setAccessUrl("/uploads" + "/" + dateDir + "/" + uniqueFilename);
        fileEntity.setFileAutoName(uniqueFilename);
        fileEntity.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        return fileEntity;
    }

    /**
     * 使用缓冲字节流保存文件到指定目录（适合大文件）
     *
     * @param file       上传的文件对象
     * @param baseDir    基础存储目录
     * @param bufferSize 缓冲区大小（字节），建议8192(8KB)或更大
     * @return FileEntity
     * @throws IOException              文件操作异常
     * @throws IllegalArgumentException 参数不合法异常
     */
    public static FileEntity uploadWithBufferedStream(MultipartFile file, String baseDir, int bufferSize) throws IOException {
        // 参数校验（与原有方法保持一致）
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (baseDir == null || baseDir.trim().isEmpty()) {
            throw new IllegalArgumentException("存储路径不能为空");
        }
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("缓冲区大小必须大于0");
        }

        // 处理相对路径（与原有方法保持一致）
        Path basePath = Paths.get(baseDir).toAbsolutePath().normalize();

        // 创建日期格式的子目录（与原有方法保持一致）
        String dateDir = DATE_FORMAT.format(new Date());
        Path uploadPath = basePath.resolve(dateDir);

        // 创建目录（如果不存在）（与原有方法保持一致）
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名（与原有方法保持一致）
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = generateUniqueFilename(fileExtension);

        // 存储文件 - 使用缓冲字节流方式（新增部分）
        Path targetPath = uploadPath.resolve(uniqueFilename);
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(targetPath);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(outputStream, bufferSize)) {

            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bufferedOutput.write(buffer, 0, bytesRead);
            }
            bufferedOutput.flush();
        }

        // 构建返回实体（与原有方法保持一致）
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileOriginalName(originalFilename);
        fileEntity.setStorageUrl(targetPath.toString());
        fileEntity.setAccessUrl("/uploads" + "/" + dateDir + "/" + uniqueFilename);
        fileEntity.setFileAutoName(uniqueFilename);
        fileEntity.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        return fileEntity;
    }

    /**
     * 下载文件
     *
     * @param filePath         文件绝对路径
     * @param originalFilename 下载时显示的文件名
     * @return ResponseEntity对象
     * @throws IOException 文件操作异常
     */
    public static ResponseEntity<Resource> download(String filePath, String originalFilename) throws IOException {
        // 参数校验
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }

        Resource resource = new FileSystemResource(file);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + encodeFilename(originalFilename));
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件绝对路径
     * @return 是否删除成功
     */
    public static boolean delete(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 生成唯一文件名
     *
     * @param extension 文件扩展名（包含.）
     * @return 唯一文件名
     */
    private static String generateUniqueFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }

    /**
     * 编码文件名（解决中文乱码问题）
     *
     * @param filename 原始文件名
     * @return 编码后的文件名
     */
    private static String encodeFilename(String filename) {
        try {
            return new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        } catch (Exception e) {
            return filename;
        }
    }

    /**
     * 获取文件大小（MB为单位）
     *
     * @param filePath 文件路径
     * @return 文件大小（MB）
     */
    public static double getFileSizeMB(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length() / (1024.0 * 1024.0);
        }
        return 0;
    }

    /**
     * 检查文件扩展名是否合法
     *
     * @param filename          文件名
     * @param allowedExtensions 允许的扩展名数组
     * @return 是否合法
     */
    public static boolean isExtensionAllowed(String filename, String[] allowedExtensions) {
        if (filename == null || allowedExtensions == null) {
            return false;
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        for (String allowedExt : allowedExtensions) {
            if (allowedExt.toLowerCase().equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查文件扩展名是否合法
     *
     * @param filename 文件名
     * @return 是否合法
     */
    public static boolean isExtensionAllowed(String filename) {
        String[] allowedExtensions = {
                // 图片
                ".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg",
                // 文档
                ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".txt",
                // 压缩包
                ".zip", ".rar",
                // 音视频
                ".mp3", ".mp4", ".wav", ".ogg"
        };
        if (filename == null) {
            return false;
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        for (String allowedExt : allowedExtensions) {
            if (allowedExt.toLowerCase().equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
