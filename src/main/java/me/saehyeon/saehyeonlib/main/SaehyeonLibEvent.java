package me.saehyeon.saehyeonlib.main;

import org.bukkit.Bukkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class SaehyeonLibEvent {

    private static final ArrayList<SaehyeonLibListener> listeners = new ArrayList<>();

    public static void register(SaehyeonLibListener listener) {
        listeners.add(listener);
    }

    public static void doEvent(Object obj) {

        for(SaehyeonLibListener listener : listeners) {
            for(Method method : listener.getClass().getDeclaredMethods()) {

                // 이벤트 발동 시의 클래스와 이 메소드의 매개변수의 클래스가 받다면, 이벤트 발동 시 호출되어야하는 메소드
                if( method.getParameterCount() == 1 && Arrays.asList(method.getParameterTypes()).contains(obj.getClass()) ) {

                    // 이 메소드 호출
                    method.setAccessible(true);

                    try {
                        method.invoke(listener, obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e+"");
                    }

                }
            }
        }
    }
}
