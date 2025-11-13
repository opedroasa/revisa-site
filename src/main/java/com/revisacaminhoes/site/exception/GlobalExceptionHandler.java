package com.revisacaminhoes.site.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Esta classe captura exceções de validação (@Valid) de toda a aplicação
 * e as formata em um JSON amigável que o frontend entende (ex: {"mensagem": "CPF inválido"}).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura a exceção lançada pelo @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Garante que a resposta seja 400
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {

        String errorMessage = "Erro de validação"; // Mensagem padrão de fallback

        // Tenta pegar a primeira mensagem de erro de campo (ex: "CPF inválido")
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }
        // Se não houver erro de campo, pega um erro global
        else if (!ex.getBindingResult().getGlobalErrors().isEmpty()) {
            errorMessage = ex.getBindingResult().getGlobalErrors().get(0).getDefaultMessage();
        }

        // Retorna um JSON no formato {"mensagem": "CPF inválido"}
        // O frontend (CompramosSeuBatido.jsx) já sabe ler "data.mensagem"
        return Map.of("mensagem", errorMessage);
    }
}