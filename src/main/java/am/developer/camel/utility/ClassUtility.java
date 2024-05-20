package am.developer.camel.utility;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public final class ClassUtility {

    private ClassUtility() {
    }

    /**
     * Retrieves Class by class name
     *
     * @param className retrieving class name
     * @return retrieved class
     */
    public static Class<?> retrieveClass(String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            log.error("The class '{}' cannot be found.", className, e);
        }
        return null;
    }

    /**
     * Finds field for the entity by field name
     *
     * @param entity    that field need to find
     * @param fieldName which need to find in the entity
     * @return found field
     */
    public static Field retrieveField(Object entity, final String fieldName) {
        final Field[] fields = entity.getClass().getDeclaredFields();
        for (final Field field : fields) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Checks is provided class has provided method
     *
     * @param clazz      class type
     * @param methodName method name that need to check
     * @return true if provided method exists in the provided class, otherwise returns false
     */
    public static boolean hasMethod(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

}
