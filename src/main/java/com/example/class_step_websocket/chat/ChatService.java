package com.example.class_step_websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
	private final ChatRepository chatRepository;
	private final SseService sseService; // DI처리

	// chat msg save
	@Transactional
	public Chat save(String msg) {
		Chat chat = Chat.builder().msg(msg).build();

		Chat savedChat = chatRepository.save(chat);
		// 핵심 : 새 메시지를 연결된 클라이언트에게 즉시 전송 처리
		sseService.broadcastMessage(savedChat.getMsg());
		//return chatRepository.save(chat);
		return savedChat;
	}

	// chat msg list
	public List<Chat> findAll() {
		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		return chatRepository.findAll(sort);
	}
}
