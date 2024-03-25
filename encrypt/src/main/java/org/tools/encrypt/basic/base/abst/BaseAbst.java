package org.tools.encrypt.basic.base.abst;

import org.tools.encrypt.basic.base.Base;

import java.util.Base64;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/25 11:36
 * @description base64加密抽象
 * @scope base
 */
@Deprecated
public abstract class BaseAbst implements Base {

    protected final Base64.Decoder decoder = Base64.getDecoder();
    protected final Base64.Encoder encoder = Base64.getEncoder();

}
