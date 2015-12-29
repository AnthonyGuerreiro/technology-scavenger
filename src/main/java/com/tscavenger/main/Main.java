package com.tscavenger.main;

import java.util.List;

import com.tscavenger.conf.Configuration;
import com.tscavenger.core.IScavengerController;
import com.tscavenger.core.ScavengerController;
import com.tscavenger.core.VisitDecider;
import com.tscavenger.core.Visitor;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class Main {

    private static Logger logger = LogManager.getInstance(Main.class);

    public static String getDefaultCrawlStorageFolder() {
        // TODO return folder based on OS
        return "C:/tmp";
    }

    public static int getDefaultNumberOfCrawlers() {
        return 7;
    }

    public static void main(String[] args) throws Exception {
        setup(args);
        IScavengerController controller = new ScavengerController(getCrawlStorageFolder(args));
        controller.addSeed("faucetface.com");
        controller.addSeed("shopify.com");

        List<String> websites = Configuration.getInstance().getWebsites();
        // for (String website : websites) {
        // System.out.println("adding seed " + website);
        // controller.addSeed(website);
        // }

        controller.start(getNumberOfCrawlers(args, websites));
        List<String> websitesUsingTechnologies = controller.getWebsitesUsingTechnologies();
        logger.info("---------------------------");
        logger.info("Websites using technologies");
        for (String website : websitesUsingTechnologies) {
            logger.info(website);
        }
    }

    private static void setup(String[] args) {
        Configuration configuration = Configuration.getInstance();
        configuration.setVisitDecider(new VisitDecider());
        configuration.setVisitor(new Visitor());
    }

    private static String getCrawlStorageFolder(String[] args) {
        if (args.length > 0) {
            return args[0];
        }
        return getDefaultCrawlStorageFolder();
    }

    private static int getNumberOfCrawlers(String[] args, List<String> websites) {

        int numberOfCrawlersArg = getNumberOfCrawlersArg(args);
        if (numberOfCrawlersArg > 0) {
            System.out.println("nr crawlers 1: " + numberOfCrawlersArg);
            return numberOfCrawlersArg;
        }

        if (websites.size() > 1000) {
            System.out.println("nr crawlers2: " + websites.size() / 100);
            return websites.size() / 100;
        }

        System.out.println("nr crawlers3: " + getDefaultNumberOfCrawlers());
        return getDefaultNumberOfCrawlers();
    }

    private static int getNumberOfCrawlersArg(String[] args) {
        if (args.length < 2) {
            return 0;
        }
        try {
            return Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
