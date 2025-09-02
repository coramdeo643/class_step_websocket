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

	// chat msg save
	@Transactional
	public Chat save(String msg) {
		Chat chat = Chat.builder().msg(msg).build();
		return chatRepository.save(chat);
	}

	// chat msg list
	public List<Chat> findAll() {
		Sort desc = Sort.by(Sort.Direction.DESC, "id");
		return chatRepository.findAll(desc);
	}
}
