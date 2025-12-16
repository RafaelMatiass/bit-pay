package br.com.bitpay.controller;

import java.io.IOException;

import br.com.bitpay.model.Cliente;
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

@WebServlet("/resgatar-investimento")
public class ResgatarInvestimentoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final InvestimentoService investimentoService = new InvestimentoServiceImpl();
    private final ContaService contaService = new ContaServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        Object usuarioLogado = (session != null)
                ? session.getAttribute("usuarioLogado")
                : null;

        if (!(usuarioLogado instanceof Cliente)) {
            response.sendRedirect("login");
            return;
        }

        Cliente cliente = (Cliente) usuarioLogado;
        Conta conta = cliente.getConta();

        if (conta == null) {
            response.sendRedirect("home");
            return;
        }

        try {
            int idAplicacao = Integer.parseInt(request.getParameter("idAplicacao"));
            int idConta = conta.getContaId();

            // 🔹 Service cuida de TUDO:
            // - valida aplicação
            // - muda status
            // - ajusta saldo no banco
            investimentoService.resgatarInvestimento(idAplicacao, idConta);

            // 🔹 Busca o saldo REAL direto do banco
            Conta contaAtualizada = contaService.buscarContaAtualizada(idConta);

            // 🔹 Atualiza sessão e cliente
            session.setAttribute("conta", contaAtualizada);
            cliente.setConta(contaAtualizada);

            session.setAttribute(
                    "mensagem",
                    "Resgate realizado com sucesso!"
            );
            session.setAttribute("tipoMensagem", "success");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute(
                    "mensagem",
                    "Erro ao resgatar investimento: " + e.getMessage()
            );
            session.setAttribute("tipoMensagem", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/investimentos");
    }
}
