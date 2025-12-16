package br.com.bitpay.controller;

import java.io.IOException;
import java.math.BigDecimal;

import br.com.bitpay.model.Conta;
import br.com.bitpay.service.InvestimentoService;
import br.com.bitpay.service.InvestimentoServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/aplicar-investimento")
public class AplicarInvestimentoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final InvestimentoService investimentoService =
            new InvestimentoServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Conta conta = (session != null)
                ? (Conta) session.getAttribute("conta")
                : null;

        if (conta == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            int idTipoInvestimento =
                    Integer.parseInt(req.getParameter("idTipoInvestimento"));

            BigDecimal valor =
                    new BigDecimal(req.getParameter("valor"));

            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("Valor inválido para investimento.");
            }

            if (conta.getSaldo().compareTo(valor) < 0) {
                throw new Exception("Saldo insuficiente.");
            }

            // 🔹 Service cuida de tudo:
            // - debita saldo
            // - cria aplicação
            investimentoService.aplicarInvestimento(
                    conta.getContaId(),
                    idTipoInvestimento,
                    valor
            );

            // 🔹 NÃO mexe no saldo aqui
            session.setAttribute(
                    "mensagem",
                    "Investimento aplicado com sucesso!"
            );
            session.setAttribute("tipoMensagem", "success");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute(
                    "mensagem",
                    "Erro ao aplicar investimento: " + e.getMessage()
            );
            session.setAttribute("tipoMensagem", "danger");
        }

        resp.sendRedirect(req.getContextPath() + "/investimentos");
    }
}
