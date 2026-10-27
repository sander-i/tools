package org.tools.example.encrypt.asymmetric;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/27
 * @since 1.0.6.1
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
