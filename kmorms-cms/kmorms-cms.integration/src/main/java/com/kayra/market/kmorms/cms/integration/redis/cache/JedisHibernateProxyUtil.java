package com.kayra.market.kmorms.cms.integration.redis.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JedisHibernateProxyUtil {

    public static void cleanBean(Object bean) throws IllegalAccessException {
        HashSet<Object> visited = new HashSet<>();

        cleanBean(bean, visited);
    }

    private static void cleanBean(Object bean, HashSet<Object> visited) throws IllegalAccessException {
        if (bean == null) {
            return;
        }

        if (bean instanceof Object[]) {
            for (Object arrayBean : (Object[]) bean) {
                cleanBean(arrayBean, visited);
            }
        } else {
            Iterator<?> iter = null;

            if (bean instanceof Collection) {
                iter = ((Collection<?>) bean).iterator();
            }

            if (iter != null) {
                while (iter.hasNext()) {
                    cleanBean(iter.next(), visited);
                }
            } else {
                if (!visited.contains(bean)) {
                    visited.add(bean);
                    processBean(bean, visited);
                }
            }
        }
    }

    private static void processBean(Object bean, HashSet<Object> visited) throws IllegalAccessException {
        Class<?> cls = bean.getClass();
        Field[] fields = cls.getDeclaredFields();

        if (fields != null) {
            for (Field field : fields) {
                cleanField(bean, visited, field);
            }
        }
    }

    private static void cleanField(Object bean, HashSet<Object> visited, Field field) throws IllegalAccessException {
        boolean flag = field.isAccessible();
        if (!flag) {
            field.setAccessible(true);
        }

        Object fieldBean = field.get(bean);
        if (!field.isAnnotationPresent(JsonIgnore.class)) {
            if (fieldBean instanceof PersistentCollection) {
                PersistentCollection persistentCollection = (PersistentCollection) fieldBean;
                if (!persistentCollection.wasInitialized()) {
                    field.set(bean, null);
                } else {
                    Collection<Object> collection = processCollection(field, bean, (Collection<?>) persistentCollection);
                    cleanBean(collection, visited);
                }
            } else {
                cleanBean(fieldBean, visited);
            }
        }

        field.setAccessible(flag);
    }

    private static Collection<Object> processCollection(Field field, Object bean, Collection<?> persistentCollection) throws IllegalAccessException {
        Collection<Object> collection = createCollection(persistentCollection);
        collection.addAll(persistentCollection);
        field.set(bean, collection);
        return collection;
    }

    private static Collection<Object> createCollection(Collection<?> persistentCollection) {
        if (persistentCollection instanceof PersistentSet) {
            return new LinkedHashSet<Object>();
        } else if (persistentCollection instanceof PersistentList) {
            return new ArrayList<Object>();
        }

        throw new NotYetImplementedException(persistentCollection.toString());
    }

}
