package br.com.bitpay.model.Enums;

public enum StatusEmprestimo {

    ATIVO(1, "Ativo"),
    QUITADO(2, "Quitado"),
    ATRASADO(3, "Atrasado");
    
    private final int codigo;
    private final String descricao;
    
    StatusEmprestimo(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public static StatusEmprestimo getByCodigo(int codigo) {
        for (StatusEmprestimo status : StatusEmprestimo.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de Status de Empréstimo inválido: " + codigo);
    }
}