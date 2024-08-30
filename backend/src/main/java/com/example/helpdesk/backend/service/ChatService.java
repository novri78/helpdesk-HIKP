package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.model.ChatMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
@Component
public class ChatService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessageToSupabase(ChatMessage message) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatMessage> request = new HttpEntity<>(message, headers);
        restTemplate.postForObject(supabaseUrl + "/rest/v1/chats", request, ChatMessage.class);
    }
}
