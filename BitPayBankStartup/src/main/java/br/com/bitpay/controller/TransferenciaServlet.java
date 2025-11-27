package br.com.bitpay.controller;

import br.com.bitpay.model.Conta;
import br.com.bitpay.service.TransferenciaService; 
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

@WebServlet("/transferencia")
public class TransferenciaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TransferenciaService transferenciaService = new TransferenciaService();
    private ContaService contaService = new ContaServiceImpl(); 
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("conta") == null) {
            response.sendRedirect("login"); 
            return;
        }

        request.getRequestDispatcher("/view/transferencia.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("conta") == null) {
            response.sendRedirect("login"); 
            return;
        }
        
        Conta contaLogada = (Conta) session.getAttribute("conta");
        
        String valorStr = request.getParameter("valorTransferencia");
        String contaDestino = request.getParameter("contaDestino");
        
        if (valorStr == null || valorStr.isEmpty() || contaDestino == null || contaDestino.isEmpty()) {
            request.setAttribute("mensagem", "Todos os campos são obrigatórios.");
            request.setAttribute("tipoMensagem", "warning");
            request.getRequestDispatcher("/view/transferencia.jsp").forward(request, response);
            return;
        }

        try {
            valorStr = valorStr.replace(",", "."); 
            BigDecimal valor = new BigDecimal(valorStr);
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                 throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
            }
            
            int idContaOrigem = contaLogada.getContaId(); 
            transferenciaService.realizarTransferencia(idContaOrigem, contaDestino, valor);
            
            Conta contaAtualizada = contaService.buscarContaAtualizada(idContaOrigem);
            
            if (contaAtualizada != null) {
                 session.setAttribute("conta", contaAtualizada); 
            }
            
            response.sendRedirect(request.getContextPath() + "/home?mensagem=Transferência de R$" + valor + " realizada com sucesso!");

        } catch (IllegalArgumentException e) {
            request.setAttribute("mensagem", "Erro: " + e.getMessage());
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/transferencia.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("mensagem", "Falha na transação: " + e.getMessage());
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/transferencia.jsp").forward(request, response);
        }
    }
}