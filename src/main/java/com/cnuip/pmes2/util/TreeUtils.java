package com.cnuip.pmes2.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUtils {
    public static <T> List<T> listToTree(List<T> items) {
        // 取第一级
        try {
            List<T> roots = items.stream().filter(t -> {
                try {
                    Method method = t.getClass().getMethod("getPreCode");
                    Object object = method.invoke(t);
                    return "".equals(object) || object==null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }).collect(Collectors.toList());
            for (T root : roots) {
                setChildren(root, items);
            }
            return roots;
        } catch (Exception e){
            return null;
        }
    }

    public static <T> void setChildren(T root, List<T> items) {
        Class<?> clazz = root.getClass();
        List<T> children = items.stream().filter(t -> {
            try {
                Method getParentId = t.getClass().getMethod("getPreCode");
                Object parentId = getParentId.invoke(t);
                Method getId = clazz.getMethod("getCode");
                Object id = getId.invoke(root);
                return id.equals(parentId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());
        if (children != null && !children.isEmpty()) {
            try {
                Method setChildren = clazz.getMethod("setChildren",List.class);
                setChildren.invoke(root, children);
                for (T child : children) {
                    setChildren(child, items);
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
