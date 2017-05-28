package ru.otus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

@SuppressWarnings("SameParameterValue")
class ReflectionHelper {
    private static List<Object> visitedObjectsList = new ArrayList<>();

    private ReflectionHelper() {
    }

    //Обход полей объекта
    static void goForFields(Object object, Field[] fields) {
        for (Field field : fields) {
            if (field.getType().isPrimitive()) {
                //do nothing
            }
            else {
                //рассматриваем не примитивы и не статические поля
                if (!isStatic(field.getModifiers())) {
                    boolean isAccessible = true;
                    try {
                        isAccessible = field.isAccessible();
                        field.setAccessible(true);
                        Object linkObject = field.get(object);
                        if (linkObject != null) {
                            if (!visitedObjectsList.contains(linkObject)) {
                                recursiveCalc(linkObject);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        if (field != null && !isAccessible) {
                            field.setAccessible(false);
                        }
                    }
                }
            }
        }
    }

    //рекурсивный вызов обхода полей ссылочного поля
    static void recursiveCalc(Object object) throws IllegalAccessException {
        visitedObjectsList.add(object);

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            goForFields(object, fields);
        }
    }

    static Object getFieldValue(Object object, String name) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = object.getClass().getDeclaredField(name);
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
        return null;
    }

}
