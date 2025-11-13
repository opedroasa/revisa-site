package com.revisacaminhoes.site.validation;

import com.revisacaminhoes.site.usecase.ValidarDocumento;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * "Cola" entre a annotation @CPF e a lógica de negócio em ValidarDocumento.
 */
public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public void initialize(CPF constraintAnnotation) {
        // Nada a inicializar
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        // Se o CPF for nulo ou vazio, não aplicamos esta validação.
        // Deixamos a @NotBlank cuidar da obrigatoriedade.
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        // Chama a lógica de negócio centralizada
        return ValidarDocumento.isValidCPF(cpf);
    }
}