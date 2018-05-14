package ru.otus;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.reflect.Modifier.isStatic;
import static ru.otus.ReflectionHelper.getFieldValue;

/**
 * Класс для преобразования объекта в JSON
 */
class JsonConverterMyImpl implements JsonConverter {

    @Override
    public String convertToJSON(Object object) {
        List<Object> visitedObjects = new ArrayList<>();
        visitedObjects.add(object);

        StringBuilder jsonStringBuilder = new StringBuilder();
        jsonStringBuilder.append("{");

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            walkObjectFields(object, fields, jsonStringBuilder, visitedObjects);
        }
        // удаляем последнюю запятую
        removeLastComma(jsonStringBuilder);

        jsonStringBuilder.append("}");

        return jsonStringBuilder.toString();
    }

    // Обход полей объекта
    private void walkObjectFields(Object object, Field[] fields, StringBuilder sb, List<Object> visitedObjects) {
        for (Field field : fields) {
            if (field.getType().isPrimitive()) {  // поле примитивного типа
                processPrimitiveField(object, sb, field);
            } else if (field.getType().equals(String.class)) {  // строка
                processStringField(object, sb, field);
            } else if (field.getType().isArray()) {  // массив
                processArrayField(object, visitedObjects, sb, field);
            } else {
                processNonPrimitiveField(object, sb, visitedObjects, field);
            }
        }
    }

    private void processPrimitiveField(Object object, StringBuilder sb, Field field) {
        String fieldName = field.getName();
        printFieldName(sb, fieldName);
        printPrimitiveFieldValue(object, sb, fieldName);
    }

    private void processStringField(Object object, StringBuilder sb, Field field) {
        String fieldName = field.getName();
        printFieldName(sb, fieldName);
        printStringFieldValue(object, sb, fieldName);
    }

    private void processArrayField(Object object, List<Object> visitedObjects, StringBuilder sb, Field field) {
        String fieldName = field.getName();
        printFieldName(sb, fieldName);
        sb.append("[");
        boolean isAccessible = true;
        try {
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            int length = Array.getLength(field.get(object));
            for (int i = 0; i < length - 1; i++) {
                Object obj = Array.get(field.get(object), i);
                if (isWrapperType(obj.getClass())) {
                    sb.append(obj);
                } else {
                    processObjectField(sb, visitedObjects, field, obj);
                }
                sb.append(",");
            }
            Object obj = Array.get(field.get(object), length - 1);
            if (isWrapperType(obj.getClass())) {
                sb.append(obj);
            } else {
                processObjectField(sb, visitedObjects, field, obj);
            }
            sb.append("]");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    private void processNonPrimitiveField(Object object, StringBuilder sb, List<Object> visitedObjects, Field field) {
        // не примитивные нестатические поля
        if (!isStatic(field.getModifiers())) {
            boolean isAccessible = true;
            try {
                isAccessible = field.isAccessible();
                field.setAccessible(true);
                Object linkObject = field.get(object);
                if (linkObject != null) {
                    if (!visitedObjects.contains(linkObject)) {
                        // Списки и множества представляем как массивы
                        if (List.class.isAssignableFrom(linkObject.getClass())) {
                            visitedObjects.add(linkObject);
                            processListField(sb, visitedObjects, field, (List<Object>) linkObject);
                        } else if (Set.class.isAssignableFrom(linkObject.getClass())) {
                            visitedObjects.add(linkObject);
                            processSetField(sb, visitedObjects, field, (Set<Object>) linkObject);
                        } else {
                            if (!field.getName().contains("this$0")) {
                                printFieldName(sb, field.getName());
                            }
                            processObjectField(sb, visitedObjects, field, linkObject);
                        }
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

    private void processListField(StringBuilder sb, List<Object> visitedObjects, Field field, List<Object> linkObject) {
        processCollectionField(sb, visitedObjects, field, linkObject.toArray());
    }

    private void processSetField(StringBuilder sb, List<Object> visitedObjects, Field field, Set<Object> linkObject) {
        Object[] objects = linkObject.toArray();
        processCollectionField(sb, visitedObjects, field, objects);
    }

    private void processObjectField(StringBuilder sb, List<Object> visitedObjects, Field field, Object linkObject) {
        if (!field.getName().contains("this$0")) {
            sb.append("{");
            walkObjectByReferenceFields(linkObject, sb, visitedObjects);
            sb.append("}");
        }
    }

    // рекурсивный вызов обхода полей ссылочного поля
    private void walkObjectByReferenceFields(Object object, StringBuilder sb, List<Object> visitedObjects) {
        visitedObjects.add(object);

        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length > 0) {
            walkObjectFields(object, fields, sb, visitedObjects);
        }
    }

    private void processCollectionField(StringBuilder sb, List<Object> visitedObjects, Field field, Object[] array) {
        printFieldName(sb, field.getName());
        sb.append("[");
        processCollectionAsArray(sb, visitedObjects, field, array);
        sb.append("]");
    }

    private void processCollectionAsArray(StringBuilder sb, List<Object> visitedObjects, Field field, Object[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            Object obj = array[i];
            if (isWrapperType(obj.getClass())) {
                sb.append(obj);
            } else {
                processObjectField(sb, visitedObjects, field, obj);
            }
            sb.append(",");
        }
        Object obj = array[array.length - 1];
        if (isWrapperType(obj.getClass())) {
            sb.append(obj);
        } else {
            processObjectField(sb, visitedObjects, field, obj);
        }
    }

    private void printFieldName(StringBuilder sb, String fieldName) {
        sb.append('"').append(fieldName).append('"').append(":");
    }

    private void printPrimitiveFieldValue(Object object, StringBuilder sb, String fieldName) {
        Object fieldValue = getFieldValue(object, fieldName);
        sb.append(fieldValue);
        sb.append(",");
    }

    private void printStringFieldValue(Object object, StringBuilder sb, String fieldName) {
        Object fieldValue = getFieldValue(object, fieldName);
        sb.append('"').append(fieldValue).append('"');
        sb.append(",");
    }

    private void removeLastComma(StringBuilder sb) {
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    private static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    // классы обёртки
    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> wrappers = new HashSet<>();
        wrappers.add(Boolean.class);
        wrappers.add(Character.class);
        wrappers.add(Byte.class);
        wrappers.add(Short.class);
        wrappers.add(Integer.class);
        wrappers.add(Long.class);
        wrappers.add(Float.class);
        wrappers.add(Double.class);
        wrappers.add(Void.class);
        return wrappers;
    }
}
