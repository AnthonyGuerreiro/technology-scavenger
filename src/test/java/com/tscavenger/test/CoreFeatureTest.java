package com.tscavenger.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.tscavenger.core.ScavengerController;
import com.tscavenger.core.TScavengerController;

public class CoreFeatureTest extends AbstractTest {

    private TScavengerController getController() {
        try {
            return new ScavengerController(getDefaultCrawlStorageFolder());
        } catch (Exception e) {
            throw new RuntimeException("Could not  setup controller", e);
        }
    }

    @Test
    public void testDetectShopify() {
        TScavengerController controller = getController();
        controller.addSeed("faucetface.com");
        controller.addTechnology("Shopify");
        controller.start(7);
        List<String> websitesUsingTechnologies = controller.getWebsitesUsingTechnologies();

        assertTrue(websitesUsingTechnologies.contains("faucetface.com"));

    }
}
