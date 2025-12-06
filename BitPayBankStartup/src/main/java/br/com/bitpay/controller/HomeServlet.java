package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.service.ContaService;
import br.com.bitpay.service.ContaServiceImpl; 

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;
    private final ContaService contaService = new ContaServiceImpl(); 

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession sessao = request.getSession(false);
        
        Object usuarioLogado = (sessao != null) ? sessao.getAttribute("usuarioLogado") : null;        
        if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
            response.sendRedirect("login"); 
            return;
        }
        
        Cliente clienteLogado = (Cliente) usuarioLogado;       
        Conta contaAntiga = clienteLogado.getConta();
        
        if (contaAntiga != null) {
            try {
                int idContaOrigem = contaAntiga.getContaId();   
                Conta contaAtualizada = contaService.buscarContaAtualizada(idContaOrigem);
                
                if (contaAtualizada != null) {
                    sessao.setAttribute("conta", contaAtualizada);                     
                    clienteLogado.setConta(contaAtualizada);
                }
                
            } catch (Exception e) {
                System.err.println("Erro ao atualizar o saldo da conta na Home: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/view/home.jsp").forward(request, response);
    }
}