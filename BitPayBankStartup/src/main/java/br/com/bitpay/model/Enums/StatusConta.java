package br.com.bitpay.model.Enums;

public enum StatusConta {
	PENDENTE(1, "Conta Pendente de Aprovação Gerencial"),
    ATIVA(2, "Conta Ativa e Operacional"),
    INATIVA(3, "Conta Encerrada ou Bloqueada");
	
	private final int codigo;
    private final String descricao;
    
    StatusConta(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public static StatusConta getByCodigo(int codigo) {
        for (StatusConta status : StatusConta.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de Status da Conta inválido: " + codigo);
    }
}
