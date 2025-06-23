package org.vniizht.suburbsweb.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Util {

    // Измеряет времени выполнения задачи в секундах
    public static float measureTime(Runnable task) {
        Date date = new Date();
        task.run();
        return (new Date().getTime() - date.getTime())/1000f;
    }

    // Форматирует дату в заданный формат
    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    // Объединяет два массива в один
    public static <T> T[] concatArrays(T[] array1, T[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    // Доводит строку до нужной длины лидирующими нулями
    public static String addLeadingZeros(String str, int length) {
        if(str == null) str = "";
        StringBuilder zeros = new StringBuilder();
        for (int i = 0; i < length - str.length(); i++) {
            zeros.append('0');
        }
        return zeros.append(str).toString();
    }

    // Доводит строку до нужной длины нулями с конца
    public static String addTrailingZeros(String str, int length) {
        if(str == null) str = "";
        StringBuilder stringBuilder = new StringBuilder(str);
        while (stringBuilder.length() < length) {
            stringBuilder.append('0');
        }
        return stringBuilder.toString();
    }

    // Парсит массив boolean из Postgres в виде строки в массив boolean
    public static boolean[] parsePostgresBooleanArray(String input) {
        if (input == null || input.isEmpty()) {
            return new boolean[0];
        }

        // Убираем фигурные скобки и разделяем элементы по запятым
        String[] elements = input.replace("{", "").replace("}", "").split(",");

        boolean[] result = new boolean[elements.length];
        for (int i = 0; i < elements.length; i++) {
            // Преобразуем "t" в true, "f" в false
            result[i] = elements[i].trim().equalsIgnoreCase("t");
        }

        return result;
    }
}
