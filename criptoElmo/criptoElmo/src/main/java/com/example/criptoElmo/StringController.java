package com.example.criptoElmo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StringController {

    private volatile String sharedString = "";

    @PostMapping("/send")
    public void receiveString(@RequestBody String string) {
        this.sharedString = string;
    }

    @GetMapping("/receive")
    public String sendString() {
        return this.sharedString;
    }
}
