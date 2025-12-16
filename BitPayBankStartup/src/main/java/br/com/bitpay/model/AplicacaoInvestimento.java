package br.com.bitpay.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AplicacaoInvestimento {

    private int id;
    private int idConta;
    private TipoInvestimento tipoInvestimento;
    private BigDecimal valorAplicado;
    private LocalDate dataAplicacao;
    private String status;

    /* ===============================
       CONSTRUTOR VAZIO (OBRIGATÓRIO)
       =============================== */
    public AplicacaoInvestimento() {
    }

    /* =========================================
       CONSTRUTOR USADO NA APLICAÇÃO (INSERT)
       ========================================= */
    public AplicacaoInvestimento(
            int idConta,
            TipoInvestimento tipoInvestimento,
            BigDecimal valorAplicado
    ) {
        this.idConta = idConta;
        this.tipoInvestimento = tipoInvestimento;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = LocalDate.now();
        this.status = "ATIVA";
    }

    /* =========================================
       CONSTRUTOR USADO PELOS DAOs (SELECT)
       ========================================= */
    public AplicacaoInvestimento(
            int id,
            int idConta,
            TipoInvestimento tipoInvestimento,
            BigDecimal valorAplicado,
            LocalDate dataAplicacao,
            String status
    ) {
        this.id = id;
        this.idConta = idConta;
        this.tipoInvestimento = tipoInvestimento;
        this.valorAplicado = valorAplicado;
        this.dataAplicacao = dataAplicacao;
        this.status = status;
    }

    /* ===============================
       GETTERS E SETTERS
       =============================== */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public TipoInvestimento getTipoInvestimento() {
        return tipoInvestimento;
    }

    public void setTipoInvestimento(TipoInvestimento tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }

    public BigDecimal getValorAplicado() {
        return valorAplicado;
    }

    public void setValorAplicado(BigDecimal valorAplicado) {
        this.valorAplicado = valorAplicado;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
