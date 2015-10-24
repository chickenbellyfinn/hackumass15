package net.halloweenapp;

/**
 * Created by Daniel on 10/24/2015.
 */
public class Main {

    private static void usage() {
        System.err.println("Usage: java -jar webapp.jar hostname port");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            usage();
        }

        try {
            String hostname = args[0];
            int port = Integer.parseInt(args[1]);

            new Thread() {
                @Override
                public void run() {
                    Server.getInstance().start(hostname, port);
                }
            }.start();
        }
        catch (NumberFormatException e) {
            usage();
        }
    }
}
