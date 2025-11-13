package br.com.fiap.globalSolution.Service;

import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.chat.prompt.Prompt; // Não precisamos mais deste import
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IaService {

    private final ChatClient chatClient;

    @Autowired
    public IaService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Gera uma Bio de perfil usando IA.
     * Isso cumpre o requisito de "Prompt Engineering".
     */
    public String gerarBio(String palavrasChave) {

        // --- INÍCIO DO PROMPT ENGINEERING ---
        String systemMessage = """
                Você é um assistente de RH especializado em criar perfis profissionais
                para uma plataforma de troca de habilidades (skill-swap).
                Sua resposta deve ser profissional, concisa (máximo 3 linhas) e
                focada nas habilidades do usuário.
                """;

        String userMessageTemplate = """
                Crie uma bio de perfil para um profissional com as seguintes
                habilidades e interesses: {keywords}
                """;
        // --- FIM DO PROMPT ENGINEERING ---

        // 1. Cria um template para o prompt do usuário
        PromptTemplate promptTemplate = new PromptTemplate(userMessageTemplate);

        // 2. Renderiza o template para uma STRING (A CORREÇÃO ESTÁ AQUI)
        String userPromptString = promptTemplate.render(Map.of("keywords", palavrasChave));

        // 3. Monta a requisição para o ChatClient com Strings
        return this.chatClient.prompt()
                .system(systemMessage) // Define a "personalidade" do assistente
                .user(userPromptString) // <-- Passa a String renderizada
                .call() // Chama a API do Vertex AI Gemini
                .content(); // Retorna o conteúdo da resposta (a bio)
    }
}