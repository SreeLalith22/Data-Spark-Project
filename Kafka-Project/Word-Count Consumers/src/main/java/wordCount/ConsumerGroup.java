package wordCount;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerGroup {

    @Autowired
    private WordCountProcess wordCountProcess;
    @KafkaListener(topics = {"wordcount-topic"}, groupId = "wordcount-group")
    public void message(String message){

        wordCountProcess.getWordCount(message);
        log.info("message: ", message);
    }
}
