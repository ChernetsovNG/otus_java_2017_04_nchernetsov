package ru.otus.measure;

import java.lang.reflect.Field;

public class MeasureFromFields {
    static long sumPrimitiveBit = 0;  //сумма по всем примитывным типам данного объекта и по всем его ссылкам
    static long sumPrimitiveByte = 0;

    public static long getObjectSize(final Object object) throws IllegalAccessException {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isPrimitive()) {
                switch (field.getType().getName()) {
                    case "byte":
                        sumPrimitiveBit += 8;
                        break;
                    case "short":
                        sumPrimitiveBit += 16;
                        break;
                    case "int":
                        sumPrimitiveBit += 32;
                        break;
                    case "long":
                        sumPrimitiveBit += 64;
                        break;
                    case "boolean":
                        sumPrimitiveBit += 8;
                        break;
                    case "char":
                        sumPrimitiveBit += 16;
                        break;
                    case "float":
                        sumPrimitiveBit += 32;
                        break;
                    case "double":
                        sumPrimitiveBit += 64;
                        break;
                }
            }
            else {
                field.setAccessible(true);
                System.out.println(field.get(object));
                System.out.println(field.getType().isArray());
            }
        }
        sumPrimitiveByte = sumPrimitiveBit / 8;
        System.out.println(sumPrimitiveByte);
        return 0L;
    }

}
