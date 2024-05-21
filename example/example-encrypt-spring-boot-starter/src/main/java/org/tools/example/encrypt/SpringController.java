package org.tools.example.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/27
 */
@Slf4j
@RestController
public class SpringController {

    @RequestMapping("/info")
    public String info() {
        return "Hello, Spring Boot!";
    }

}
