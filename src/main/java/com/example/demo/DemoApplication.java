package com.example.demo;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Component
class EmitterTest {
	private final EmitterProcessor<Message<Integer>> emitter = EmitterProcessor.create();

	@Bean
	public Consumer<String[]> consume() {
		return (strings) -> {
			for (String string: strings) {
				Message<Integer> message =
						MessageBuilder.withPayload(Integer.valueOf(string))
						.setHeader("spring.cloud.stream.sendto.destination", "destination")
						.build();
				emitter.onNext(message);
			}
		};
	}

	@Bean
	public Supplier<Flux<Message<Integer>>> supply() {
		return () -> emitter;
	}
}
