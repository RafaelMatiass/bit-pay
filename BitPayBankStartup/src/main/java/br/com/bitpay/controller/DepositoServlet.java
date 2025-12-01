package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.service.DepositoService; 
import br.com.bitpay.service.ContaService;
import br.com.bitpay.service.ContaServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/deposito")
public class DepositoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private DepositoService depositoService = new DepositoService();
    private ContaService contaService = new ContaServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;

        if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
            response.sendRedirect("login"); 
            return;
        }

        request.getRequestDispatcher("/view/deposito.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;
        
        if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
            response.sendRedirect("login"); 
            return;
        }        
        
        Conta contaLogada = (Conta) session.getAttribute("conta");
        
        String valorStr = request.getParameter("valor"); 
        
        if (valorStr == null || valorStr.isEmpty()) {
            request.setAttribute("mensagem", "Valor do depósito é obrigatório.");
            request.setAttribute("tipoMensagem", "warning");
            request.getRequestDispatcher("/view/deposito.jsp").forward(request, response);
            return;
        }

        try {
            valorStr = valorStr.replace(",", "."); 
            BigDecimal valor = new BigDecimal(valorStr);
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                 throw new IllegalArgumentException("O valor deve ser positivo.");
            }
            
            int idConta = contaLogada.getContaId(); 
            
            depositoService.realizarDeposito(idConta, valor);
            
            Conta contaAtualizada = contaService.buscarContaAtualizada(idConta);
            
            if (contaAtualizada != null) {
                 session.setAttribute("conta", contaAtualizada); 
            }
            
            response.sendRedirect(request.getContextPath() + "/home?mensagem=Depósito realizado com sucesso!");

        } catch (IllegalArgumentException e) {
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/deposito.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("mensagem", "Falha na transação: " + e.getMessage());
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/deposito.jsp").forward(request, response);
        }
    }
}