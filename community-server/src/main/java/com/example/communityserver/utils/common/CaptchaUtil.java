package com.example.communityserver.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * <p>
 * 验证码图片工具
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-24
 **/


public class CaptchaUtil {
    // 验证码字符集（移除了容易混淆的字符）
    private static final char[] chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789".toCharArray();
    // 验证码长度
    private static final int CODE_LENGTH = 4;
    // 图片宽度（增大）
    private static final int WIDTH = 160;
    // 图片高度（增大）
    private static final int HEIGHT = 60;
    // 字体大小（增大）
    private static final int FONT_SIZE = 32;
    // 字符间距（增大）
    private static final int CHAR_SPACING = 30;

    public static Captcha generate() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置背景色（更柔和的白色）
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制干扰线（减少数量但加粗）
        Random random = new Random();
        g.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < 6; i++) {
            g.setColor(getRandomColor());
            g.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT),
                    random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }

        // 生成验证码
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            String s = String.valueOf(chars[random.nextInt(chars.length)]);
            sb.append(s);

            // 设置字体（使用更清晰的字体）
            g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));

            // 随机倾斜角度（-15到15度）
            double theta = (random.nextDouble() - 0.5) * Math.PI / 8;
            g.rotate(theta, CHAR_SPACING * i + 15, HEIGHT / 2 + 5);

            // 绘制字符
            g.setColor(getRandomDarkColor());
            g.drawString(s, CHAR_SPACING * i + 10, HEIGHT / 2 + 10);

            // 重置旋转
            g.rotate(-theta, CHAR_SPACING * i + 15, HEIGHT / 2 + 5);
        }

        // 添加噪点（减少数量）
        int area = (int) (0.02f * WIDTH * HEIGHT);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            image.setRGB(x, y, getRandomColor().getRGB());
        }

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            return new Captcha(sb.toString(), "data:image/jpeg;base64," + base64Image);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码失败", e);
        }
    }

    // 生成深色（适合文字）
    private static Color getRandomDarkColor() {
        Random random = new Random();
        return new Color(
                random.nextInt(100),       // R: 0-100
                random.nextInt(100),       // G: 0-100
                random.nextInt(100)        // B: 0-100
        );
    }

    // 生成任意颜色（适合背景干扰）
    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
    }

    // 返回数据类型
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Captcha {
        private final String code;
        private final String image;
    }
}
