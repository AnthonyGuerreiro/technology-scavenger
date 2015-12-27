package com.tscavenger.main;

import com.tscavenger.core.ScavengerController;
import com.tscavenger.core.TScavengerController;

public class Main {

    public static String getDefaultCrawlStorageFolder() {
        // TODO return folder based on OS
        return "C:/tmp";
    }

    public static int getDefaultNumberOfCrawlers() {
        return 7;
    }

    public static void main(String[] args) throws Exception {
        TScavengerController controller = new ScavengerController(getCrawlStorageFolder(args));
        controller.addSeed("faucetface.com");
        controller.start(getNumberOfCrawlers(args));
    }

    private static String getCrawlStorageFolder(String[] args) {
        if (args.length > 0) {
            return args[0];
        }
        return getDefaultCrawlStorageFolder();
    }

    private static int getNumberOfCrawlers(String[] args) {
        try {
            if (args.length > 1) {
                return Integer.parseInt(args[1]);
            }
            return getDefaultNumberOfCrawlers();
        } catch (NumberFormatException e) {
            return getDefaultNumberOfCrawlers();
        }
    }
}
