package br.com.bitpay.service;

import java.math.BigDecimal;
import java.util.List;
import br.com.bitpay.model.TipoInvestimento;
public interface InvestimentoService {
	
	 List<TipoInvestimento> listarTiposInvestimento() throws Exception;
	 
	 void aplicarInvestimento(int idConta, int idTipoInvestimento, BigDecimal valor) throws Exception;
}