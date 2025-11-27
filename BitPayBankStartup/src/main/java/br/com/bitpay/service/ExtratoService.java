package br.com.bitpay.service;

import java.time.LocalDate;
import java.util.List;

import br.com.bitpay.model.Movimentacao;

public interface ExtratoService {
    List<Movimentacao> gerarExtrato(int idConta, LocalDate dataInicial, LocalDate dataFinal);
}