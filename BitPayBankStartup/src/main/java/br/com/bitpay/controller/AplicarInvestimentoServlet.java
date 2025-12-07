package br.com.bitpay.controller;

import br.com.bitpay.service.InvestimentoService;
import br.com.bitpay.service.InvestimentoServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/aplicarInvestimento")
public class AplicarInvestimentoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    

    private final InvestimentoService investimentoService = new InvestimentoServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        
        String idContaStr = request.getParameter("idConta");
        String idTipoInvestimentoStr = request.getParameter("idTipoInvestimento");
        String valorAplicacaoStr = request.getParameter("valorAplicacao");
        
        String mensagem = null;
        String tipoMensagem = "danger"; 
        try {
          
            int idConta = Integer.parseInt(idContaStr);
            int idTipoInvestimento = Integer.parseInt(idTipoInvestimentoStr);
            
           
            BigDecimal valorAplicacao = new BigDecimal(valorAplicacaoStr);
            
           
            investimentoService.aplicarInvestimento(idConta, idTipoInvestimento, valorAplicacao);
            
           
            mensagem = "Aplicação em investimento realizada com sucesso! O valor de " 
                       + valorAplicacao.toPlainString() + " foi debitado de sua conta.";
            tipoMensagem = "success";
            
        } catch (NumberFormatException e) {
            mensagem = "Erro de entrada: O valor ou o ID não é um número válido.";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
         
            mensagem = e.getMessage();
        } catch (Exception e) {
           
            mensagem = "Erro de sistema. A transação não pôde ser completada. " + e.getMessage();
            e.printStackTrace();
        }
        
       
        request.getSession().setAttribute("mensagem", mensagem);
        request.getSession().setAttribute("tipoMensagem", tipoMensagem);
        
     
        response.sendRedirect("investimentos"); 
    }
}