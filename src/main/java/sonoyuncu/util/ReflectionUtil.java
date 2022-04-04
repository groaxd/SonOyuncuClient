package sonoyuncu.util;

import java.lang.reflect.Field;
/**
 * @author Groax
 * @since 4/3/2022
 */
public class ReflectionUtil {

    public static <T> T get(Field field, Object accessor) {
        try {
            field.setAccessible(true);
            return (T) field.get(accessor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void set(Field field, Object accessor, Object value) {
        field.setAccessible(true);
        try {
            field.set(accessor, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Field getField(String name, Class<?> clazz) {
        while (clazz != null)
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }

        throw new RuntimeException("Can't find " + name);
    }

}
