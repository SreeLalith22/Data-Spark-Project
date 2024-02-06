package wordCount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wordCount.kafka.KafkaProducer;

@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/post")
    public ResponseEntity<?> publishMessage(@RequestBody String message){

        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Published successfully");
    }
}
