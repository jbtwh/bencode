package ru.y.bencode;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ReflectionUtils {

    public static List<Class> getTypesRecur(Class c) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        List<Class> fields = new ArrayList<Class>();
        fields.add(c);

        for(PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(c, Object.class).getPropertyDescriptors())
            if (propertyDescriptor.getReadMethod()!=null && propertyDescriptor.getWriteMethod()!=null) {
                Class pc = propertyDescriptor.getPropertyType();
                fields.add(pc);
                if (pc.isAnnotationPresent(BencodeSerializable.class))
                    fields.addAll(getTypesRecur(pc));
            }

        return fields;
    }

    public static Map<String, Object> getFieldsValues(Object o) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        //fields that have getters/setters and their values
        Map<String, Object> fields = new LinkedHashMap<String,Object>();

        for(PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(o.getClass(), Object.class).getPropertyDescriptors())
            if (propertyDescriptor.getReadMethod()!=null && propertyDescriptor.getWriteMethod()!=null)
                fields.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(o));

        return fields;
    }

    public static void setValuesForFields(Map<String, Object> vals, Object o) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Object value;
        for(PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(o.getClass(), Object.class).getPropertyDescriptors())
            if (propertyDescriptor.getReadMethod()!=null && propertyDescriptor.getWriteMethod()!=null && (value =vals.get(propertyDescriptor.getName()))!=null){
                Class c = propertyDescriptor.getWriteMethod().getParameterTypes()[0];
                Object nested = null;
                if (c.isAnnotationPresent(BencodeSerializable.class) && value instanceof Map){
                    nested = c.newInstance();
                    setValuesForFields((Map) value, nested);
                    value = nested;
                }
                else if(value instanceof Map) {
                    Map<String, Object> m = (Map) value;
                    Map<String, Object> m2 = new LinkedHashMap<String, Object>();

                    Type t =  propertyDescriptor.getWriteMethod().getGenericParameterTypes()[0];
                    if (t instanceof ParameterizedType) {
                        Class keyType = (Class) ((ParameterizedType) t).getActualTypeArguments()[0];
                        Class valType = (Class) ((ParameterizedType) t).getActualTypeArguments()[1];

                        if(keyType!=String.class) throw new RuntimeException("key is not string "+keyType);
                        if(valType.isAnnotationPresent(BencodeSerializable.class)){
                            for(Map.Entry e : m.entrySet()) {
                                nested = valType.newInstance();
                                setValuesForFields((Map) e.getValue(), nested);
                                m2.put((String) e.getKey(), nested);
                            }
                            value = m2;
                        }
                    }
                }
                else if(value instanceof List) {
                    List<Object> l = (List) value;
                    List<Object> l2 = new ArrayList<Object>();

                    Type t =  propertyDescriptor.getWriteMethod().getGenericParameterTypes()[0];
                    if (t instanceof ParameterizedType) {
                        Class type = (Class) ((ParameterizedType) t).getActualTypeArguments()[0];

                        if(type.isAnnotationPresent(BencodeSerializable.class)){
                            for(Object e : l) {
                                nested = type.newInstance();
                                setValuesForFields((Map) e, nested);
                                l2.add(nested);
                            }
                            value = l2;
                        }
                    }
                }

                propertyDescriptor.getWriteMethod().invoke(o, value);
            }
    }
}
