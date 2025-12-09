package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.ParcelaEmprestimo;
import br.com.bitpay.service.EmprestimoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/emprestimo")
public class EmprestimoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private EmprestimoService emprestimoService = new EmprestimoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
	HttpSession session = request.getSession(false);
	
	Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;
	
	if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
	    response.sendRedirect("login"); 
	    return;
	}
        request.getRequestDispatcher("/view/emprestimo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	HttpSession session = request.getSession(false);
        
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;
        
        if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
            response.sendRedirect("login"); 
            return;
        } 

        try {
            BigDecimal valorEntrada = new BigDecimal(request.getParameter("entrada"));
            BigDecimal valorTotal = new BigDecimal(request.getParameter("valor"));
            BigDecimal taxaJuros = new BigDecimal(request.getParameter("taxaJuros")); 
            int numeroParcelas = Integer.parseInt(request.getParameter("parcelas"));
            
            BigDecimal saldoDevedorEfetivo = valorTotal.subtract(valorEntrada);

            List<ParcelaEmprestimo> simulacao = emprestimoService.simularEmprestimo(
                valorTotal, 
                valorEntrada,
                taxaJuros, 
                numeroParcelas, 
                LocalDate.now()
            );

            request.setAttribute("simulacao", simulacao);
            request.setAttribute("valorTotal", valorTotal);
            request.setAttribute("valorEntrada", valorEntrada); 
            request.setAttribute("saldoDevedorEfetivo", saldoDevedorEfetivo); 
            request.setAttribute("taxaJuros", taxaJuros);
            request.setAttribute("numeroParcelas", numeroParcelas);
            
            request.getRequestDispatcher("/view/emprestimo.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("mensagemErro", "Erro na simulação: " + e.getMessage());
            request.getRequestDispatcher("/view/emprestimo.jsp").forward(request, response);
        }
    }
}