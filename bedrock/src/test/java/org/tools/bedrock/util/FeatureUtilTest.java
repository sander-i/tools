package org.tools.bedrock.util;

import junit.framework.TestCase;
import org.tools.bedrock.http.resp.v2.R;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/08/04
 */
public class FeatureUtilTest extends TestCase {

    public void testFeatureToggle() {
        R<String> r = R.ok("pro");
        R<String> r2 = R.ok("sandbox");
        boolean toggle = false;
        final R<String> result = FeatureUtil.featureToggle(() -> r, () -> r2, toggle);
        System.out.println(result);
        toggle = true;
        final R<String> result1 = FeatureUtil.featureToggle(() -> r, () -> r2, toggle);
        System.out.println(result1);
    }

}