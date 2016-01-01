package com.tscavenger.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import com.tscavenger.core.IScavengerController;
import com.tscavenger.core.ScavengerController;

public class CoreFeatureTest extends AbstractTest {

    private IScavengerController getController() {
        try {
            setup();
            return new ScavengerController();
        } catch (Exception e) {
            throw new RuntimeException("Could not setup controller", e);
        }
    }

    // TODO implement test while crawler runs
    // @Test
    public void testDetectShopify() {
        IScavengerController controller = getController();
        List<String> websitesUsingTechnologies = controller.getWebsitesUsingTechnologies();

        assertTrue(websitesUsingTechnologies.contains("faucetface.com"));

    }
}
