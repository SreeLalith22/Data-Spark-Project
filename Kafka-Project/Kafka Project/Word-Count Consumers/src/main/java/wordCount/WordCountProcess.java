package wordCount;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.tomcat.util.net.jsse.JSSEUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.*;

@Service
@Slf4j
public class WordCountProcess {

    public void getWordCount(String wordcount){

        String [] words = wordcount.toLowerCase().split("[^a-zA-Z0-9']+");

        Map<String, Integer> wordCount = new HashMap<>();

        for(String word: words){

            if(!word.isEmpty()) {
                Integer count = wordCount.get(word);

                if (!wordCount.containsKey(word)) {

                    wordCount.put(word, 1);
                } else {

                    wordCount.put(word, count + 1);
                }
            }
        }
        log.info("Word Count: {}", wordCount);
        for (String key: wordCount.keySet()) {
            System.out.println(key + ',' + wordCount.get(key));
        }
    }



}
