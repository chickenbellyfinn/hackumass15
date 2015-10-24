package net.halloweenapp;

/**
 * Created by Daniel on 10/24/2015.
 */
public class Main {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                Server.getInstance().start();
            }
        }.start();
    }
}
