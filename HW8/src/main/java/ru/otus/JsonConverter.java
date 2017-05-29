package ru.otus;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;
import static ru.otus.utils.ReflectionHelper.getFieldValue;

class JsonConverter {
    String objectToJson(Object object) {
        List<Object> visitedObjects = new ArrayList<>();
        visitedObjects.add(object);

        StringBuilder jsonStringBuilder = new StringBuilder();
        jsonStringBuilder.append("{");

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            walkObjectFields(object, fields, jsonStringBuilder, visitedObjects);
        }
        //удаляем последнюю запятую
        removeLastComma(jsonStringBuilder);

        jsonStringBuilder.append("}");

        return jsonStringBuilder.toString();
    }

    private void removeLastComma(StringBuilder sb) {
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length()-1);
        }
    }

    //Обход полей объекта
    private void walkObjectFields(Object object, Field[] fields, StringBuilder sb, List<Object> visitedObjects) {
        for (Field field : fields) {
            // поле примитивного типа
            if (field.getType().isPrimitive()) {
                processPrimitiveField(object, sb, field);
            } else if (field.getType().equals(String.class)) {  // строка
                processStringField(object, sb, field);
            } else if (field.getType().isArray()) {  // массив
                processArrayField(object, sb, field);
            } else {
                //не примитивные нестатические поля
                if (!isStatic(field.getModifiers())) {
                    boolean isAccessible = true;
                    try {
                        isAccessible = field.isAccessible();
                        field.setAccessible(true);
                        Object linkObject = field.get(object);
                        if (linkObject != null) {
                            if (!visitedObjects.contains(linkObject)) {
                                walkObjectByReferenceFields(linkObject, sb, visitedObjects);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        if (!isAccessible) {
                            field.setAccessible(false);
                        }
                    }
                }
            }
        }
    }

    private void processArrayField(Object object, StringBuilder sb, Field field) {
        String fieldName = field.getName();
        printFieldName(sb, fieldName);
        sb.append("[");
        boolean isAccessible = true;
        try {
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            int length = Array.getLength(field.get(object));
            for (int i = 0; i < length - 1; i ++) {
                Object arrayElement = Array.get(field.get(object), i);
                sb.append(arrayElement).append(",");
            }
            Object arrayElement = Array.get(field.get(object), length - 1);
            sb.append(arrayElement).append("]");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    private void processStringField(Object object, StringBuilder sb, Field field) {
        String fieldName = field.getName();
        printFieldName(sb, fieldName);
        printStringFieldValue(object, sb, fieldName);
    }

    private void processPrimitiveField(Object object, StringBuilder sb, Field field) {
        String fieldName = field.getName();
        printFieldName(sb, fieldName);
        printPrimitiveFieldValue(object, sb, fieldName);
    }

    private void printFieldName(StringBuilder sb, String fieldName) {
        sb.append('"').append(fieldName).append('"').append(":");
    }

    private void printPrimitiveFieldValue(Object object, StringBuilder sb, String fieldName) {
        Object fieldValue =  getFieldValue(object, fieldName);
        sb.append(fieldValue);
        sb.append(",");
    }

    private void printStringFieldValue(Object object, StringBuilder sb, String fieldName) {
        Object fieldValue =  getFieldValue(object, fieldName);
        sb.append('"').append(fieldValue).append('"');
        sb.append(",");
    }

    //рекурсивный вызов обхода полей ссылочного поля
    private void walkObjectByReferenceFields(Object object, StringBuilder sb, List<Object> visitedObjects) {
        visitedObjects.add(object);

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            walkObjectFields(object, fields, sb, visitedObjects);
        }
    }
}
