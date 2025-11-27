package br.com.bitpay.service;

import java.time.LocalDate;
import java.util.List;

import br.com.bitpay.dao.MovimentacaoDAO;
import br.com.bitpay.model.Movimentacao;

public class ExtratoServiceImpl implements ExtratoService {

    private final MovimentacaoDAO dao;

    public ExtratoServiceImpl() {
        this.dao = new MovimentacaoDAO();
    }

    @Override
    public List<Movimentacao> gerarExtrato(int idConta, LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null || dataInicial.isAfter(dataFinal)) {
            return List.of(); 
        }

        return dao.buscarPorContaEPeriodo(idConta, dataInicial, dataFinal);
    }
}