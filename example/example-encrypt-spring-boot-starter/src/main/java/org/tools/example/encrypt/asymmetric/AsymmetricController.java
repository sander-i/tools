package org.tools.example.encrypt.asymmetric;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tools.encrypt.entity.Encrypt;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/27
 */
@Slf4j
@RestController
@RequestMapping("/asymmetric")
public class AsymmetricController {

//    @Autowired
//    private Encrypt encrypt;

    @RequestMapping("/info")
    public String info() {
        return "Hello, Spring Boot!";
    }

}
