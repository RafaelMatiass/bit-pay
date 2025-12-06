package br.com.bitpay.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.TipoInvestimento;
public interface InvestimentoService {
	
	 List<TipoInvestimento> listarTiposInvestimento() throws Exception;
	 
	 void aplicarInvestimento(int idConta, int idTipoInvestimento, BigDecimal valor) throws Exception;
	 Conta buscarContaPorIdUsuario(int idUsuario) throws Exception; 
	 BigDecimal resgatarInvestimento(int idAplicacao, int idConta) throws Exception; 
	 List<AplicacaoInvestimento> listarPortfolio(int idConta) throws Exception;
}