package br.com.fiap.globalSolution.Rabbit.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailDto {

    // Declaração dos componentes do e-mail (referência, de quem, para quem, assunto e texto)
    // @NotBlank pois são campos obrigatórios no e-mail; se não estiver completo, retorna Bad Request

    @NotBlank
    private String ownerRef;

    @NotBlank
    @Email
    private String emailFrom;

    @NotBlank
    @Email
    private String emailTo;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}
