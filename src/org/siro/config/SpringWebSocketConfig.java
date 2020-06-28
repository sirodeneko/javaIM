package org.siro.config;

import org.siro.handler.SpringWebSocketHandler;
import org.siro.interceptor.SpringWebSocketHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {


	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler(),"/ws")
				.addInterceptors(new SpringWebSocketHandlerInterceptor()).setAllowedOrigins("*");

		registry.addHandler(webSocketHandler(), "/sockjs/socketServer")
				.addInterceptors(new SpringWebSocketHandlerInterceptor()).withSockJS();
	}

	@Bean
	public TextWebSocketHandler webSocketHandler(){

		return new SpringWebSocketHandler();
	}

}