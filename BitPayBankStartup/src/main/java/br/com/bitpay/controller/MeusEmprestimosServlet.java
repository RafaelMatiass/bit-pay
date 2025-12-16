package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.service.EmprestimoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/meus-emprestimos")
public class MeusEmprestimosServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EmprestimoService emprestimoService = new EmprestimoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Object usuarioLogado = session.getAttribute("usuarioLogado");

        if (!(usuarioLogado instanceof Cliente cliente)) {
            resp.sendRedirect("login");
            return;
        }

        try {
            int idConta = cliente.getConta().getContaId();

            var emprestimos = emprestimoService.listarEmprestimosPorConta(idConta);

            req.setAttribute("emprestimos", emprestimos);
            req.getRequestDispatcher("/view/meus-emprestimos.jsp")
                .forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao carregar empréstimos: " + e.getMessage());
            req.getRequestDispatcher("/view/meus-emprestimos.jsp")
                .forward(req, resp);
        }
    }
}
