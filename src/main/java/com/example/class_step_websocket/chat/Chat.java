package com.example.class_step_websocket.chat;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_tb")
@Getter
@NoArgsConstructor
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String msg;

	@Builder
	public Chat(Integer id, String msg) {
		this.id = id;
		this.msg = msg;
	}
}
