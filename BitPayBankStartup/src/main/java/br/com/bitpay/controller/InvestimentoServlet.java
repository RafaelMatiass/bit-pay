package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.TipoInvestimento;
import br.com.bitpay.service.InvestimentoService;
import br.com.bitpay.service.InvestimentoServiceImpl;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/investimentos")
public class InvestimentoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    
    private final InvestimentoService investimentoService = new InvestimentoServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession sessao = request.getSession(false);
        
       
        if (sessao == null || sessao.getAttribute("usuarioLogado") == null || sessao.getAttribute("conta") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        try {
        	
        	
        	 Cliente clienteLogado = (Cliente) sessao.getAttribute("usuarioLogado");
             Conta contaLogada = (Conta) sessao.getAttribute("conta");

      
            List<TipoInvestimento> tipos = investimentoService.listarTiposInvestimento();

           
           
            String mensagem = (String) sessao.getAttribute("mensagem");
            String tipoMensagem = (String) sessao.getAttribute("tipoMensagem");

    
            sessao.removeAttribute("mensagem");
            sessao.removeAttribute("tipoMensagem");

         
            request.setAttribute("tiposInvestimento", tipos);
            request.setAttribute("saldoConta", contaLogada.getSaldo().toPlainString());
            request.setAttribute("idConta", contaLogada.getContaId());
            request.setAttribute("mensagem", mensagem);
            request.setAttribute("tipoMensagem", tipoMensagem);

         
            request.getRequestDispatcher("/view/Investimentos.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Erro ao carregar a lista de investimentos.");
            request.getRequestDispatcher("/view/home.jsp").forward(request, response);
        }
    }
    
   
}