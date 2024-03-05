package com.example.hilibrary.util;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;

public class HiViewUtil {
    public static <T> T findTypeView(@NonNull ViewGroup group, Class<T> cls) {
        if (group == null) {
            return null;
        }
        Deque<View> deque = new ArrayDeque<>();
        deque.add(group);
        while (!deque.isEmpty()) {
            View node = deque.removeFirst();
            if (cls.isInstance(node)) {
                return cls.cast(node);
            } else if (node instanceof ViewGroup) {
                ViewGroup container = (ViewGroup) node;
                for (int i = 0, count = container.getChildCount(); i < count; i++) {
                    deque.add(container.getChildAt(i));
                }
            }
        }
        return null;
    }
}
