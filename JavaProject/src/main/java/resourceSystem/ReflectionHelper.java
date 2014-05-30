package resourceSystem;

import java.lang.reflect.Field;

/**
 * Created by artur on 30.05.14.
 */
public class ReflectionHelper {
    public static Object createInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | InstantiationException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setFieldValue(Object object,
                                     String fieldName,
                                     String value) {

        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            if (field.getType().equals(String.class)) {
                field.set(object, value);
            } else if (field.getType().equals(int.class)) {
                field.set(object, Integer.decode(value));
            }

            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
