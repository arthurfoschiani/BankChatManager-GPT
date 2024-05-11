package br.com.fiap.bankmanager;

import java.util.List;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    
    @Autowired
    OpenAiChatClient chat;

    public String sendMessage(String message) {
        SystemMessage systemMessage = new SystemMessage("""
            Você é um gerente do Banco Safra na linha Private de clientes (segmento de clientes com mais dinheiro).
            Caso vc não saiba respostas das perguntas que os clientes te fizeram, diga que vc precisa consultar seu supervisor para responder a pergunta.
            Quero que vc simule que é um gerente real dando informações ficticiais como se vc realmente fosse.
            Não responda nada que não seja relacionado com o assunto de ser gerente do banco.
        """);

        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        ChatResponse response = chat.call(prompt);
        System.out.println(response);        

        return response.getResult().getOutput().getContent();
    }

}
