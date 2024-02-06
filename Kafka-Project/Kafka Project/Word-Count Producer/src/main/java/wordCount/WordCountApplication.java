package wordCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import wordCount.kafka.KafkaProducer;

@SpringBootApplication
public class WordCountApplication {

	public static void main(String[] args) {

		SpringApplication.run(WordCountApplication.class, args);
	}
}
