package com.example.class_step_websocket.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseService {
	// SSE 연결을 저장하는 Map 자료구조
	private final ConcurrentHashMap<String, SseEmitter> emitterHashMap = new ConcurrentHashMap<>();

	// Time out setting
	private static final long TIMEOUT = 1000L * 60 * 5;

	// 새로운 SSE 연결 생성 기능
	public SseEmitter createConnection(String clientId) {
		// 1. 객체 생성
		SseEmitter emitter = new SseEmitter(TIMEOUT);

		// 2. 연결 정보를 가진 객체를 자료구조 map에 저장
		emitterHashMap.put(clientId, emitter);

		// 3. 연결 완료 / 타임아웃 / 에러 시 Callback 등록
		log.info("New connection request - SSE object created");
		emitter.onCompletion(() -> {
			log.info("Connection request success!");
		});

		// Timeout, remove client id
		emitter.onTimeout(() -> {
			emitterHashMap.remove(clientId);
			log.error("SSE Timeout!");
		});

		emitter.onError(throwable -> {
			emitterHashMap.remove(clientId);
			log.error("SSE Connection Error!");
		});

		try {
			emitter.send(SseEmitter.event().name("connect").data("Connection Success!"));
		} catch (IOException e) {
			log.error("SSE Connection Error! : {}", clientId);
		}

		return emitter;
	}

	// 모든 연결된 클라이언트에게 메시지 보내기(브로드캐스트)
	public void broadcastMessage(String msg) {
		// 자료구조에 들어가있는 객체들을 가지고 오자
		emitterHashMap.forEach((clientId, sseEmitter) -> {
			try {
				sseEmitter.send(SseEmitter.event().name("newMessage").data(msg));
			} catch (IOException e) {
				log.warn("Message send error!");
				emitterHashMap.remove(clientId);
			}
		});
	}
}
