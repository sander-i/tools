package org.tools.encrypt.entity.symmetrical;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * @author sander
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeaProperties extends EncryptProperties {

    {
        setAlgorithm(AlgorithmEnum.IDEA.getAlgorithm());
        /*
        IDEA/ECB/ISO10126Padding
         */
        setAlgorithmMode(AlgorithmModeEnum.IDEA_ECB_ISO10126Padding.getAlgorithmMode());
        setKeyBits(128);
    }

}
