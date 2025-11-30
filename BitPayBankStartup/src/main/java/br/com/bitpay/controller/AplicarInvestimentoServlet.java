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
    
    // Instanciação do Service Layer
    private final InvestimentoService investimentoService = new InvestimentoServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Coleta e Parsing de Dados
        // O idConta e idTipoInvestimento vêm como String e precisam ser convertidos para int.
        String idContaStr = request.getParameter("idConta");
        String idTipoInvestimentoStr = request.getParameter("idTipoInvestimento");
        String valorAplicacaoStr = request.getParameter("valorAplicacao");
        
        String mensagem = null;
        String tipoMensagem = "danger"; // Assume erro por padrão
        
        try {
            // Conversão de IDs e Valor
            int idConta = Integer.parseInt(idContaStr);
            int idTipoInvestimento = Integer.parseInt(idTipoInvestimentoStr);
            
            // Usamos BigDecimal para garantir precisão monetária
            BigDecimal valorAplicacao = new BigDecimal(valorAplicacaoStr);
            
            // 2. Chamada ao Service Layer (Transação)
            investimentoService.aplicarInvestimento(idConta, idTipoInvestimento, valorAplicacao);
            
            // 3. Sucesso
            mensagem = "Aplicação em investimento realizada com sucesso! O valor de " 
                       + valorAplicacao.toPlainString() + " foi debitado de sua conta.";
            tipoMensagem = "success";
            
        } catch (NumberFormatException e) {
            mensagem = "Erro de entrada: O valor ou o ID não é um número válido.";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Captura erros de Negócio (Saldo insuficiente, Valor Mínimo, etc.)
            mensagem = e.getMessage();
        } catch (Exception e) {
            // Captura erros de Sistema/Banco de Dados (Rollback)
            mensagem = "Erro de sistema. A transação não pôde ser completada. " + e.getMessage();
            e.printStackTrace();
        }
        
        // 4. Redirecionar com Mensagem
        // É crucial fazer um 'redirect' para evitar re-submissão do formulário (F5)
        // O redirect deve voltar para o InvestimentoController (doGet) para recarregar a lista de produtos.
        
        // Adicionamos a mensagem à sessão para sobreviver ao redirect (Padrão POST-REDIRECT-GET)
        request.getSession().setAttribute("mensagem", mensagem);
        request.getSession().setAttribute("tipoMensagem", tipoMensagem);
        
        // Redireciona para o Controller principal de Investimentos
        response.sendRedirect("investimentos"); 
    }
}