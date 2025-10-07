package es.maetd.bff.external.infrastructure.adapters.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BffExternalWebController {

    @GetMapping("/test")
    public void testEndpoint() {}
}
