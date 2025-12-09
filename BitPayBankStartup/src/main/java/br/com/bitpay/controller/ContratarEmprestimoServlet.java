package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Emprestimo;
import br.com.bitpay.model.ParcelaEmprestimo;
import br.com.bitpay.model.Enums.StatusEmprestimo;
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

@WebServlet("/contratar-emprestimo")
public class ContratarEmprestimoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private EmprestimoService emprestimoService = new EmprestimoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;

        if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
            response.sendRedirect("login"); 
            return;
        }
        
        Cliente clienteLogado = (Cliente) usuarioLogado;
        
        try {
            BigDecimal valorTotal = new BigDecimal(request.getParameter("valorTotal"));
            BigDecimal valorEntrada = new BigDecimal(request.getParameter("valorEntrada")); 
            BigDecimal taxaJuros = new BigDecimal(request.getParameter("taxaJuros"));
            int numeroParcelas = Integer.parseInt(request.getParameter("parcelas"));
            
            List<ParcelaEmprestimo> parcelas = emprestimoService.simularEmprestimo(
                valorTotal, 
                valorEntrada, 
                taxaJuros, 
                numeroParcelas, 
                LocalDate.now()
            );
            
            int idContaOrigem = clienteLogado.getConta().getContaId(); 
            
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setValorTotal(valorTotal);
            emprestimo.setValorEntrada(valorEntrada);
            emprestimo.setDataContratacao(LocalDate.now());
            emprestimo.setNumerosParcelas(numeroParcelas);
            emprestimo.setTaxaJuros(taxaJuros);
            emprestimo.setIdConta(idContaOrigem); 
            emprestimo.setStatus(StatusEmprestimo.ATIVO);
            emprestimo.setParcelas(parcelas); 

            emprestimoService.contratarEmprestimo(emprestimo);

            request.getSession().setAttribute("mensagemSucesso", "Empréstimo de " + valorTotal + " contratado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/home");

        } catch (Exception e) {
            System.err.println("Erro na transação de contratação (ROLLBACK): " + e.getMessage());
            e.printStackTrace(); 

            request.getSession().setAttribute("mensagemErro", "Falha ao contratar: " + e.getMessage());
            
            response.sendRedirect(request.getContextPath() + "/emprestimo");
        }
    }
}