package com.revisacaminhoes.site.usecase;

import org.springframework.stereotype.Component;

/**
 * Lógica de negócio para validação de documentos.
 * (Ainda não é um @Service, mas sim um @Component utilitário
 * ou apenas uma classe com métodos estáticos)
 */
@Component // Ou pode usar métodos estáticos se preferir
public class ValidarDocumento {

    /**
     * Valida um CPF.
     * @param cpf String contendo o CPF (com ou sem máscara)
     * @return true se o CPF for válido, false caso contrário
     */
    public static boolean isValidCPF(String cpf) {
        if (cpf == null) {
            return false;
        }

        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("\\D", "");

        // 1. Verifica tamanho
        if (cpfLimpo.length() != 11) {
            return false;
        }

        // 2. Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // 3. Cálculo do primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpfLimpo.charAt(i) - '0') * (10 - i);
            }
            int resto = (soma * 10) % 11;
            int dv1 = (resto == 10 || resto == 11) ? 0 : resto;

            if (dv1 != (cpfLimpo.charAt(9) - '0')) {
                return false;
            }

            // 4. Cálculo do segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpfLimpo.charAt(i) - '0') * (11 - i);
            }
            resto = (soma * 10) % 11;
            int dv2 = (resto == 10 || resto == 11) ? 0 : resto;

            return dv2 == (cpfLimpo.charAt(10) - '0');

        } catch (Exception e) {
            return false;
        }
    }

    // Você pode adicionar public static boolean isValidCNPJ(String cnpj) aqui no futuro
}