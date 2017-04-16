package ru.otus.util;

import java.util.List;

public class Utils {
    public static void printList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("List: ");
        if (list.size() > 0) {
            for (int i = 0; i < list.size()-1; i++) {
                sb.append(list.get(i).toString());
                sb.append(", ");
            }
            sb.append(list.get(list.size()-1));
        }
        System.out.println(sb.toString());
    }
}
