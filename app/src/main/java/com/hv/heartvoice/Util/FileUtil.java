package com.hv.heartvoice.Util;

public class FileUtil {

    public static String formatFileSize(long data){
        if(data > 0){
            double size = (double)data;
            double kiloByte = size / 1024;
            if (kiloByte < 1 && kiloByte > 0) {
                return size + "Byte";
            }
            double megaByte = kiloByte / 1024;
            if (megaByte < 1) {
                return String.format("%.2fK", kiloByte);
            }
            double gigaByte = megaByte / 1024;
            if (gigaByte < 1) {
                return String.format("%.2fM", megaByte);
            }
            double teraByte = gigaByte / 1024;
            if (teraByte < 1) {
                return String.format("%.2fG", gigaByte);
            }
            return String.format("%.2fT", teraByte);
        }
        return "OK";
    }

}
