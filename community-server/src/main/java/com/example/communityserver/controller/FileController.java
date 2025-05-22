package com.example.communityserver.controller;

import com.example.communityserver.entity.po.FileEntity;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.utils.Result;
import com.example.communityserver.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 上传下载接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-28
 **/

@Api(tags = "文件的上传和下载")
@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private IFileEntityService fileService;

    @ApiOperation("单文件上传")
    @PostMapping("/upload")
    public Result<FileEntity> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件是空");
        }
        if (!StringUtil.isValidFileName(file.getOriginalFilename())) {
            return Result.error("非法文件名");
        }
        FileEntity fileEntity = fileService.uploadFile(file);
        // 将本地地址清空
        fileEntity.setStorageUrl(null);
        System.out.println(fileEntity);
        return Result.success(fileEntity);
    }

    @ApiOperation("通过文件名删除文件")
    @DeleteMapping("/delFileById")
    public Result delFileById(@RequestParam("fileId") Long id) {
        return fileService.delFileById(id) ? Result.success() : Result.error("删除失败");
    }

}
