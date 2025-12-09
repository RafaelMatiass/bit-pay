package br.com.bitpay.model.Enums;

public enum StatusParcelasEmprestimo {

    ABERTA(1, "Aberta"),
    PAGA(2, "Paga"),
    ATRASADA(3, "Atrasada");
    
    private final int codigo;
    private final String descricao;
    
    StatusParcelasEmprestimo(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public static StatusParcelasEmprestimo getByCodigo(int codigo) {
        for (StatusParcelasEmprestimo status : StatusParcelasEmprestimo.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de Status de Parcela inválido: " + codigo);
    }
}