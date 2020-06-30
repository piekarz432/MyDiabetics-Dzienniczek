package com.karol.piekarski.mydiabetics_dzienniczek.Java.Class;

public class CheckLoginSingleton {
    private volatile static CheckLoginSingleton instance;

    public boolean isLoggedGoogle;

    private CheckLoginSingleton() {
    }

    public static CheckLoginSingleton getInstance() {
        if (instance == null) {
            synchronized (CheckLoginSingleton.class) {
                if (instance == null) {
                    instance = new CheckLoginSingleton();
                }
            }
        }

        return instance;
    }
}
