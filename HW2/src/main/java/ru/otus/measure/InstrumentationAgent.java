package ru.otus.measure;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

public class InstrumentationAgent {
    private static int BYTE_IN_KB = 1024;
    private static int BYTE_IN_MB = BYTE_IN_KB * 1024;

    private static long complexSize = 0;  // размер объекта и всех его подъобъектов по ссылкам
    private static List<Object> visitedObjectsList = new ArrayList<>();

    private static volatile Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    /**
     * Основной метод вычисления размера объекта
     * @param object - объект
     * @return размер объекта в байтах
     */
    public static long getObjectComplexSize(Object object) {
        complexSize = 0;
        visitedObjectsList.clear();

        complexSize += getObjectSize(object);
        visitedObjectsList.add(object);

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            goForFields(object, fields);
        }

        return complexSize;
    }

    // стандартный метод вычисления размера одиночного объекта
    private static long getObjectSize(final Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return instrumentation.getObjectSize(object);
    }

    // Обход полей объекта
    private static void goForFields(Object object, Field[] fields) {
        for (Field field : fields) {
            //рассматриваем не примитивные поля
            if (!field.getType().isPrimitive()) {
                // рассматриваем не статические поля
                if (!isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    try {
                        Object linkObject = field.get(object);
                        if (linkObject != null) {
                            if (!visitedObjectsList.contains(linkObject)) {
                                recursiveCalc(linkObject);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // рекурсивный вызов обхода полей для поля ссылочного типа
    private static void recursiveCalc(Object object) throws IllegalAccessException {
        complexSize += getObjectSize(object);
        visitedObjectsList.add(object);

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            goForFields(object, fields);
        }
    }

    public static void printObjectSizeByte(final Object object) {
        System.out.println("Object: " + trimString(object.toString(), 15) + " of type: " + object.getClass() +
            " has size of: " + getObjectComplexSize(object) + " bytes\n");
    }

    public static void printObjectSizeMb(final Object object) {
        System.out.println("Object: " + trimString(object.toString(), 15) + " of type: " + object.getClass() +
            " has size of: " + ((double) getObjectComplexSize(object)) / BYTE_IN_MB + " Mb\n");
    }

    private static String trimString(String str, int limit) {
        return str.length() > limit ? str.substring(0, limit) + "..." : str;
    }

}
