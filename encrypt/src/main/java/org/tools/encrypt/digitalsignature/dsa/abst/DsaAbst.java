package org.tools.encrypt.digitalsignature.dsa.abst;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.digitalsignature.dsa.Dsa;
import org.tools.encrypt.digitalsignature.dsa.DsaProperties;

import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:33
 * @description dsa签名抽象类
 * @scope dsa
 */
public abstract class DsaAbst implements Dsa {

    private DsaProperties dsaProperties;
    private Signature signature;

    @Override
    public DsaProperties getDsaProperties() {
        return dsaProperties;
    }

    @Override
    public Dsa setDsaProperties(DsaProperties dsaProperties) {
        this.dsaProperties = dsaProperties;
        return this;
    }

    protected Signature getSignature() throws NoSuchAlgorithmException {
        if (BaseUtils.isNull(signature)) {
            signature = Signature.getInstance(DsaProperties.SIGN_ALGORITHM);
        }
        return signature;
    }

}
