package com.example.communityserver.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @program: springsecurity
 * @description:
 * @author: DongGuoFeiFei
 * @create: 2024-07-10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private Long fileId;
    private String nickname;
    @Pattern(regexp = "^[\\p{L}]{2,20}$", message = "姓名格式不正确(不允许有空格)")
    private String username;
    private String password;
    private String email;
    private String phone;
    private String isActive;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String lastLogin;
}
