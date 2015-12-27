package com.tscavenger.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.tscavenger.core.IScavengerController;
import com.tscavenger.core.ScavengerController;

public class CoreFeatureTest extends AbstractTest {

    private IScavengerController getController() {
        try {
            setup();
            return new ScavengerController(getDefaultCrawlStorageFolder());
        } catch (Exception e) {
            throw new RuntimeException("Could not setup controller", e);
        }
    }

    @Test
    public void testDetectShopify() {
        IScavengerController controller = getController();
        controller.addSeed("faucetface.com");
        controller.start(7);
        List<String> websitesUsingTechnologies = controller.getWebsitesUsingTechnologies();

        assertTrue(websitesUsingTechnologies.contains("faucetface.com"));

    }
}
