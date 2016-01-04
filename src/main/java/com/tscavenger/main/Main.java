package com.tscavenger.main;

import com.tscavenger.conf.Configuration;
import com.tscavenger.core.IScavengerController;
import com.tscavenger.core.ScavengerController;
import com.tscavenger.core.URLFollowDecider;
import com.tscavenger.core.Visitor;
import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class Main {

    private static Logger logger = LogManager.getInstance(Main.class);

    public static void main(String[] args) throws Exception {
        setup(args);
        IScavengerController controller = new ScavengerController();
        controller.start();

        // TODO list websites using technologies while crawler runs

        // List<String> websitesUsingTechnologies =
        // controller.getWebsitesUsingTechnologies();
        // logger.info("---------------------------");
        // logger.info("Websites using technologies");
        // for (String website : websitesUsingTechnologies) {
        // logger.info(website);
        // }
    }

    private static void setup(String[] args) {
        Configuration configuration = Configuration.getInstance();
        configuration.setVisitDecider(new URLFollowDecider());
        configuration.setVisitor(new Visitor());
    }

}
