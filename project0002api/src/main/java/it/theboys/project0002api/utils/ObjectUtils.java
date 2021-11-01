package it.theboys.project0002api.utils;


import it.theboys.project0002api.exception.database.ImmutableFieldException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectUtils<T> {

    public T updateObjectFromObject(T updatingObj, T sourceObj, String[] filter) throws ImmutableFieldException {
        Method[] methods = updatingObj.getClass().getMethods();
        List<String> filterList= new ArrayList<>(Arrays.asList(filter));
        List<String> setterList = filterList.stream().map(
                ObjectUtils::generateSetter
        ).collect(Collectors.toList());
        for (Method fromMethod : methods) {
            if (analyzeMethod(updatingObj, fromMethod)) {
                String toName = getSetterName(fromMethod);
                //if method should be invoked skip
                if (!setterList.isEmpty() && setterList.contains(toName)){
                    continue;
                }
                try {
                    invokeNonNull(updatingObj, sourceObj, fromMethod, toName, setterList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return updatingObj;
    }

    private void invokeNonNull(T updatingObj, T sourceObj, Method fromMethod, String toName, List<String> setterList) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method toMethod = updatingObj.getClass().getMethod(toName, fromMethod.getReturnType());
        Object value = fromMethod.invoke(sourceObj, (Object[]) null);
        if (value != null)
        {
            toMethod.invoke(updatingObj, value);
        }
    }

    public static String generateSetter(String field) {
        StringBuilder setter = new StringBuilder("set");
        char[] arr = field.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return setter.append(arr).toString();

    }

    private String getSetterName(Method fromMethod) {
        String methodName = fromMethod.getName();
        if (methodName.startsWith("get"))
            return methodName.replace("get", "set");
        else return methodName.replace("is", "set");
    }

    private Boolean analyzeMethod(T updatingObj, Method fromMethod) {
        boolean isGetter;
        // check if method name is in object methods list
        if (!fromMethod.getDeclaringClass().equals(updatingObj.getClass())) {
            return false;
        }
        // check if method is Getter
        isGetter = fromMethod.getName().startsWith("get") || fromMethod.getName().startsWith("is");
        return isGetter;
    }

}
