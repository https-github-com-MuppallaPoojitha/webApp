package neu.csye6225.webappone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthController {

    @GetMapping("/health/v1")
    public ResponseEntity checkStatus() {
        return new ResponseEntity(HttpStatus.OK);
    }
}