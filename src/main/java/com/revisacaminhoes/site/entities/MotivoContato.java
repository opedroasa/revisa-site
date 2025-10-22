package com.revisacaminhoes.site.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.text.Normalizer;
import java.util.Locale;

public enum MotivoContato {
    ORCAMENTO,              // Orçamento de peças
    DISPONIBILIDADE,        // Disponibilidade/compatibilidade de peça
    PAGAMENTO_FRETE,        // Pagamento, frete e prazos
    GARANTIA_POS_VENDA,     // Pós-venda / garantia
    DEVOLUCAO_TROCA,        // Troca / Devolução
    PARCERIA,               // Parcerias comerciais
    RECLAMACAO,             // Reclamação
    OUTRO;                  // Outro

    /** Aceita valores “humanos” tipo "garantia pós-venda", "pagamento-frete", etc. */
    @JsonCreator
    public static MotivoContato from(String value) {
        if (value == null) return null;
        String v = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toUpperCase(Locale.ROOT)
                .replace(' ', '_')
                .replace('-', '_');
        // tenta nome exato primeiro
        for (MotivoContato m : values()) {
            if (m.name().equalsIgnoreCase(value) || m.name().equals(v)) return m;
        }
        throw new IllegalArgumentException("Motivo inválido: " + value);
    }
}
