package com.example.communityserver;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://123.207.189.85:3306/db_community?useSSL=false&serverTimezone=Asia/Shanghai";
        try (Connection conn = DriverManager.getConnection(url, "root", "20021118")) {
            System.out.println("连接成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
