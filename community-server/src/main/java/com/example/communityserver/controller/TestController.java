//package com.example.communityserver.controller;
//
//
//import com.example.communityserver.utils.web.Result;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/test")
//public class TestController {
//
//    @GetMapping("/1")
//    @PreAuthorize("@vip.myAuthority('superAdmin')")
//    public Result test1() {
//        return Result.success(200, "获取成功", "test1");
//    }
//
//    @GetMapping("/2")
//    @PreAuthorize("@vip.myAuthority('admin')")
//    public Result test2() {
//        return Result.success(200, "获取成功", "test2");
//    }
//
//    @GetMapping("/3")
//    @PreAuthorize("@vip.myAuthority('user')")
//    public Result test3() {
//        return Result.success(200, "获取成功", "test3");
//    }
//
//    @GetMapping("/4")
//    @PreAuthorize("@vip.myAuthority('visitor')")
//    public Result test4() {
//        return Result.success(200, "获取成功", "test4");
//    }
//
//}
