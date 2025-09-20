package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.http.resp.R;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/08/04
 */
public class FeatureUtilTest {

    @Test
    public void testFeatureToggle() {
        R<String> r = R.ok("pro");
        R<String> r2 = R.ok("sandbox");

        // Test when toggle is false, should return the first supplier result
        boolean toggle = false;
        final R<String> result = FeatureUtil.featureToggle(() -> r, () -> r2, toggle);
        assertEquals("pro", result.getData());

        // Test when toggle is true, should return the second supplier result
        toggle = true;
        final R<String> result1 = FeatureUtil.featureToggle(() -> r, () -> r2, toggle);
        assertEquals("sandbox", result1.getData());
    }

}