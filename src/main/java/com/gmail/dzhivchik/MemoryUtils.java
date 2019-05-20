package com.gmail.dzhivchik;

public class MemoryUtils {
    public static String[] showBusySpace(long filesSize) {
        long ostatok = 0;
        String[] sizes = new String[2];
        int size = 0;
        long wholePart = filesSize;
        long delitel = 1;
        int pow = 0;

        while (wholePart / 1024 > 0) {
            wholePart = wholePart / 1024;
            delitel = delitel * 1024;
            pow++;
        }
        ostatok = filesSize % delitel;
        size = (int) Math.round(Double.parseDouble(wholePart + "." + ostatok));

        switch (pow) {
            case 0:
                if (filesSize != 0) {
                    sizes[0] = size + " bytes";
                }
                break;
            case 1:
                sizes[0] = size + " Kb";
                break;
            case 2:
                sizes[0] = size + " Mb";
                break;
            case 3:
                sizes[0] = size + " Gb";
                break;
        }
        //Доступное по условиям тарифа. Пока захардкожено
        sizes[1] = "10 Gb";
        return sizes;
    }
}
