package com.sleepwalker.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QrCodeUtils {

    /**
     * 生成二维码图片 不存储 直接以流的形式输出到页面
     * 
     * @param content
     * @param response
     */
    public static BufferedImage encodeQrcodeImage(String content, int width, int height) {
        BufferedImage image = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集编码类型
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height,
                hints);
            image = toBufferedImage(bitMatrix);
        } catch (WriterException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return image;
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
}
