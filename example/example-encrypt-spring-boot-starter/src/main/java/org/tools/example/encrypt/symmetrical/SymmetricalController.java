package org.tools.example.encrypt.symmetrical;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tools.encrypt.entity.Encrypt;
import org.tools.encrypt.entity.symmetrical.Symmetrical;
import org.tools.encrypt.exception.symmetrical.des.DesException;
import org.tools.encrypt.symmetrical.des.DesHelper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/27
 */
@Slf4j
@RestController
@RequestMapping("/symmetrical")
public class SymmetricalController {

    @Autowired
    private Encrypt encrypt;

    @RequestMapping("des/load")
    public String desLoad() throws DesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        DesHelper.load(encrypt.getSymmetrical().getDes());
//        final Encrypt encrypt = tools.getEncrypt();
//        final Symmetrical symmetrical = encrypt.getSymmetrical();
        return DesHelper.getProperties().getSecretKey();
    }

    @RequestMapping("/des/encode")
    public String desEncode(@RequestParam("raw") String raw) throws DesException {
        return DesHelper.encode(raw);
    }

    @RequestMapping("/des/decode")
    public String desDecode(@RequestParam("encode") String encode) throws DesException {
        return DesHelper.decode(encode);
    }

}
