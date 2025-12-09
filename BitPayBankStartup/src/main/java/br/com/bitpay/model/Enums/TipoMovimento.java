package br.com.bitpay.model.Enums;

public enum TipoMovimento {
	DEPOSITO(1, "Depósito"),
	SAQUE(2, "Saque"),
	TRANSFERENCIA_ENVIADA(3, "Transferência Enviada"),
	TRANSFERENCIA_RECEBIDA(4, "Transferência Recebida"),
	INVESTIMENTO(5, "Investimento"),
	RESGATE(6, "Resgate Investimento"),
	EMPRESTIMO(7, "Emprestimo");
//	PAGAMENTO_EMPRESTIMO(6, "Pagamento de Empréstimo"); Precisa ser implemtado

	private final int id;
	private final String descricao;

	TipoMovimento(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoMovimento fromId(int id) {
		for (TipoMovimento tipo : TipoMovimento.values()) {
			if (tipo.id == id) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("ID de TipoMovimento inválido: " + id);
	}
}