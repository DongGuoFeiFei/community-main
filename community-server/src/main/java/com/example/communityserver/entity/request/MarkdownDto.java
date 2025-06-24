package com.example.communityserver.entity.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * markdown文本数据
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-28
 **/

@Data
public class MarkdownDto {
    private Integer userId;
    private String markdownText;
    private MultipartFile[] images;
}
