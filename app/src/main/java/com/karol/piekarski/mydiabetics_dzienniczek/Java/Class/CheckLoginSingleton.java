package com.karol.piekarski.mydiabetics_dzienniczek.Java.Class;

public class Singleton {
    private volatile static Singleton instance;

    public boolean isLoggedGoogle;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }

        return instance;
    }
}
