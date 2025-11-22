package br.com.bitpay.model.Enums;

public enum TipoUsuario {

	CLIENTE(1, "Cliente"),
    GERENTE(2, "Gerente");
	
	private final int codigo;
    private final String descricao;
	
    TipoUsuario(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public static TipoUsuario getByCodigo(int codigo) {
        for (TipoUsuario perfil : TipoUsuario.values()) {
            if (perfil.getCodigo() == codigo) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Código de Perfil de Usuário inválido: " + codigo);
    }
	
}
