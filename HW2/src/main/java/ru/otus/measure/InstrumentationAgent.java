package ru.otus.measure;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

public class InstrumentationAgent {
    static int bitInByte = 8;
    static int ByteInKb = 1024;
    static int byteInMb = ByteInKb * 1024;

    private static long complexSize = 0;  //размер объекта и всех его подъобъектов по ссылкам
    private static List<Object> visitedObjectsList = new ArrayList<>();

    private static volatile Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static String trimString(String str, int limit) {
        return (str.length() > limit ? str.substring(0, limit)+"..." : str);
    }

    public static void printObjectSizeByte(final Object object) {
        System.out.println("Object: " + trimString(object.toString(), 15) + " of type: " + object.getClass() +
                " has size of: " + getObjectComplexSize(object) + " bytes\n");
    }

    public static void printObjectSizeMb(final Object object) {
        System.out.println("Object: " + trimString(object.toString(), 15) + " of type: " + object.getClass() +
                " has size of: " + ((double) getObjectComplexSize(object))/byteInMb + " Mb\n");
    }

    //Основной метод вычисления размера
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

    //Обход полей объекта
    private static void goForFields(Object object, Field[] fields) {
        for (Field field : fields) {
            if (field.getType().isPrimitive()) {
                //do nothing
            }
            else {
                //рассматриваем не примитивы и не статические поля
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

    //рекурсивный вызов обхода полей ссылочного поля
    public static void recursiveCalc(Object object) throws IllegalAccessException {
        complexSize += getObjectSize(object);
        visitedObjectsList.add(object);

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            goForFields(object, fields);
        }
    }

    //стандартный метод вычисления размера одиночного объекта
    public static long getObjectSize(final Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return instrumentation.getObjectSize(object);
    }

}
