package br.com.bitpay.controller;

import br.com.bitpay.model.AplicacaoInvestimento;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.service.InvestimentoService;
import br.com.bitpay.service.InvestimentoServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/meus-investimentos")
public class PortifolioController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final InvestimentoService investimentoService = new InvestimentoServiceImpl();

    private void carregarPortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession sessao, int idConta) 
            throws ServletException, IOException {
        
        try {
           
            List<AplicacaoInvestimento> portfolio = investimentoService.listarPortfolio(idConta);
            
            String mensagem = (String) sessao.getAttribute("mensagem");
            String tipoMensagem = (String) sessao.getAttribute("tipoMensagem");
            
            if (mensagem != null) {
                request.setAttribute("mensagem", mensagem);
                request.setAttribute("tipoMensagem", tipoMensagem);
                sessao.removeAttribute("mensagem");
                sessao.removeAttribute("tipoMensagem");
            }
            
       
            request.setAttribute("portfolio", portfolio);
            
            request.getRequestDispatcher("/view/meus_investimentos.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro ao carregar o portfólio: " + e.getMessage());
            request.getRequestDispatcher("/view/home.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession sessao = request.getSession(false);
        if (sessao == null || sessao.getAttribute("usuarioLogado") == null || sessao.getAttribute("conta") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
        	Cliente clienteLogado = (Cliente) sessao.getAttribute("usuarioLogado");
            Conta conta = investimentoService.buscarContaPorIdUsuario(clienteLogado.getId());
            
            if (conta == null) throw new Exception("Conta não encontrada.");

           
            sessao.setAttribute("idContaLogada", conta.getContaId());
            request.setAttribute("idConta", conta.getContaId());
            
           
            carregarPortfolio(request, response, sessao, conta.getContaId());

        } catch (Exception e) {
             e.printStackTrace();
             request.setAttribute("mensagemErro", "Erro ao preparar o portfólio: " + e.getMessage());
             request.getRequestDispatcher("/view/home.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession sessao = request.getSession(false);
        String idAplicacaoStr = request.getParameter("idAplicacao");
        String idContaStr = request.getParameter("idConta");
        
        String mensagem = null;
        String tipoMensagem = "danger"; 
        
        try {
            int idAplicacao = Integer.parseInt(idAplicacaoStr);
            int idConta = Integer.parseInt(idContaStr);
            
            BigDecimal valorResgate = investimentoService.resgatarInvestimento(idAplicacao, idConta);
            
           
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            mensagem = "Resgate de investimento realizado com sucesso! O valor de " 
                       + nf.format(valorResgate) + " foi creditado em sua conta.";
            tipoMensagem = "success";
            
        } catch (NumberFormatException e) {
            mensagem = "Erro de entrada: ID inválido.";
        } catch (IllegalArgumentException e) {
          
            mensagem = e.getMessage();
        } catch (Exception e) {
         
            mensagem = "Erro de sistema. A transação de resgate falhou.";
            e.printStackTrace();
        }
       
        sessao.setAttribute("mensagem", mensagem);
        sessao.setAttribute("tipoMensagem", tipoMensagem);
        response.sendRedirect("meus-investimentos"); 
    }
}