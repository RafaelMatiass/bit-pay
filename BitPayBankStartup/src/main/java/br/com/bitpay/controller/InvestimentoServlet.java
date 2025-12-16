package br.com.bitpay.controller;

import br.com.bitpay.model.Conta;
import br.com.bitpay.service.ContaService;
import br.com.bitpay.service.ContaServiceImpl;
import br.com.bitpay.service.InvestimentoService;
import br.com.bitpay.service.InvestimentoServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/investimentos")
public class InvestimentoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final InvestimentoService investimentoService =
            new InvestimentoServiceImpl();

    private final ContaService contaService =
            new ContaServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Conta contaSessao = (session != null)
                ? (Conta) session.getAttribute("conta")
                : null;

        if (contaSessao == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            // 🔹 BUSCA A CONTA REAL DO BANCO
            Conta contaAtualizada =
                    contaService.buscarContaAtualizada(contaSessao.getContaId());

            // 🔹 ATUALIZA A SESSÃO
            session.setAttribute("conta", contaAtualizada);

            // 🔹 USA SEMPRE O SALDO ATUALIZADO
            req.setAttribute("saldoConta", contaAtualizada.getSaldo());

            req.setAttribute(
                    "tiposInvestimento",
                    investimentoService.listarTiposInvestimento()
            );

            req.setAttribute(
                    "portfolio",
                    investimentoService.listarPortfolio(contaAtualizada.getContaId())
            );

            req.getRequestDispatcher("/view/investimentos-caixinhas.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(
                    "Erro ao carregar página de investimentos", e
            );
        }
    }
}
