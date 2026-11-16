package io.github.sander_i.encrypt.entity.symmetrical;


import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

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
