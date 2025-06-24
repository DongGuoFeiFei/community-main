package com.example.communityserver.controller;

import com.example.communityserver.entity.model.FileEntity;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.utils.web.Result;
import com.example.communityserver.utils.common.StringUtil;
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
        return Result.success(fileEntity);
    }

    @ApiOperation("通过文件id删除文件")
    @PostMapping("/delFileById/{id}")
    public Result delFileById(@PathVariable Long id) {
        System.out.println(id);
        // 判断发起人是否是文件上传者本人
        // 删除文件
        return fileService.delFileById(id) ? Result.success() : Result.error("未找到文件,删除失败");
    }
}
