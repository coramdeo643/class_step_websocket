package com.example.class_step_websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // view 반환
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	// msg 작성 form page
	@GetMapping("/save-form")
	public String saveForm() {
		return "save-form";
	}

	// chat list page
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("models", chatService.findAll());
		return "index";
	}

	// chat msg save request
	@PostMapping("/chat")
	public String save(String msg) {
		chatService.save(msg);
		// POST Mapping (웹에서 주의할점) - 중복 Post 방지
		// PRG : Post-Redirect-Get pattern
		// return "redirect:/";
		return "save-form";
	}

}
