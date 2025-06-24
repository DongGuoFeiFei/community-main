package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-29
 **/


public interface IFileEntityService extends IService<FileEntity> {
    FileEntity uploadFile(MultipartFile file);

    boolean delFileById(Long id);
}
