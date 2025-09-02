package com.example.handler;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class TimerHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(TimerHandler.class);
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	// Websocket connection이 완료되면 호출된다.
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		log.info("ConnectionEstablished: {}", session.getId());
	}

	// Connection 맺은 session과 네트워크 에러가 발생하면 호출된다.
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		log.error("TransportError: [{}] from {}", exception.getMessage(), session.getId());
	}

	// 접속이 종료되면 호출된다.
	@Override
	public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
		log.info("ConnectionClosed: [{}] from {}", status, session.getId());
	}

	// 연결된 Websocket에 데이터가 수신되면 호출된다.
	@Override
	protected void handleTextMessage(WebSocketSession session, @NonNull TextMessage message) {
		log.info("Received TextMessage: [{}] from {}", message, session.getId());

		try {
			long seconds = Long.parseLong(message.getPayload());
			long timestamp = Instant.now().toEpochMilli();

			// 람다 안에서 던지는 예외는 메서드에 선언된 예외로 전파되지 않기 때문에 람다 내부에서 try-catch로 처리해야 한다.
			// 하지만 람다 내부에서 try-catch로 처리하게 될 경우 코드가 복잡해지기 때문에 별도의 함수로 분리해서 처리한다.
			scheduledExecutorService.schedule(
				() -> sendMessage(session, String.format("%d에 등록한 %d초 타이머 완료.", timestamp, seconds)), seconds,
				TimeUnit.SECONDS); // 시차가 흐른뒤에 두번째로 가는 메시지

			sendMessage(session, String.format("%d에 등록한 %d초 타이머 등록 완료.", timestamp, seconds));
		} catch (Exception ex) {
			sendMessage(session, "정수가 아님. 타이머 등록 실패.");
		}
	}

	private void sendMessage(WebSocketSession session, String message) {
		try {
			session.sendMessage(new TextMessage(message));
			log.info("send message: {} to {}", message, session.getId());
		} catch (Exception ex) {
			log.error("메시지 전송 실패 to {} error: {}}", session.getId(), ex.getMessage());
		}
	}
}
