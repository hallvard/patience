package no.hal.patience.fx.util;

import java.lang.reflect.InvocationTargetException;

import javafx.beans.value.WritableValue;

public class FxUtil {

    public static <T> void setProperties(String propertyName, Class<T> clazz, T value, Iterable<? extends Object> propertyOwners) {
        for (Object o : propertyOwners) {
            try {
                WritableValue<T> pileViewProp = (WritableValue<T>) o.getClass().getMethod(propertyName + "Property")
                        .invoke(o);
                pileViewProp.setValue(value);
            } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException
                    | NoSuchMethodException | SecurityException e) {
            }
        }
    }
}