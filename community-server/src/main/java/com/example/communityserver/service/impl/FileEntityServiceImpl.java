package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.FileEntity;
import com.example.communityserver.mapper.FileEntityMapper;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.utils.file.FileUtils;
import com.example.communityserver.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-29
 **/

@Service
public class FileEntityServiceImpl extends ServiceImpl<FileEntityMapper, FileEntity> implements IFileEntityService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    private FileEntityMapper fileMapper;

    @Override
    public FileEntity uploadFile(MultipartFile file) {
        try {
            // 上传文件
            FileEntity fileEntity = FileUtils.upload(file, uploadPath);
            fileEntity.setUserId(SecurityUtils.getLoginUserId());
            // 保存到数据库
            if (fileMapper.insert(fileEntity) > 0) {
                return fileEntity;
            }
            throw new RuntimeException("文件信息保存失败");

        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delFileById(Long id) {
        LambdaQueryWrapper<FileEntity> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(FileEntity::getUserId, SecurityUtils.getLoginUserId()).eq(FileEntity::getFileId, id);
        FileEntity fileEntity = fileMapper.selectOne(queryWrapper);
        // 数据库没有找到文件
        if (fileEntity == null) {
            return false;
        }
        // 数据库中找到文件，删除文件
        if (FileUtils.delete(fileEntity.getStorageUrl())) {
            return fileMapper.deleteById(id) > 0;
        }
        return false;
    }
}

