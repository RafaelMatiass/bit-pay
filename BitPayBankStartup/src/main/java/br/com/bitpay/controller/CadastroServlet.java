package br.com.bitpay.controller;

import br.com.bitpay.service.ContaService;
import br.com.bitpay.service.ContaServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; 
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cadastro") 
public class CadastroServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    private final ContaService contaService = new ContaServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
       
        String cpf = request.getParameter("cpf");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String confirmarSenha = request.getParameter("confirmarSenha");
        String nome = request.getParameter("nome");
        String dataNascimentoStr = request.getParameter("dataNascimento");
        String telefone = request.getParameter("telefone");
        String cep = request.getParameter("cep");
        String logradouro = request.getParameter("logradouro");
        String numeroStr = request.getParameter("numero"); 
        String bairro = request.getParameter("bairro");
        String cidade = request.getParameter("cidade");
        String estado = request.getParameter("estado");
        
      
        if (!senha.equals(confirmarSenha)) {
            request.setAttribute("mensagem", "Erro: As senhas digitadas não coincidem.");
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/cadastro.jsp").forward(request, response);
            return;
        }
        
     
        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
             request.setAttribute("mensagem", "Erro: Formato de data de nascimento inválido.");
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/cadastro.jsp").forward(request, response);
            return;
        }

        try {
            
            contaService.abrirConta(cpf, email, senha, nome, dataNascimento, telefone, cep, logradouro, numeroStr, bairro, cidade, estado);
            
          
            request.getSession().setAttribute("mensagem", "Cadastro enviado com sucesso! Sua conta está PENDENTE de aprovação gerencial. Aguarde...");
            request.getSession().setAttribute("tipoMensagem", "success");
            
           
            request.getRequestDispatcher("index.jsp").forward(request, response); 
            
        } catch (IllegalArgumentException e) {
           
            request.setAttribute("mensagem", "Erro de Negócio: " + e.getMessage());
            request.setAttribute("tipoMensagem", "danger");
           
            request.getRequestDispatcher("/view/cadastro.jsp").forward(request, response);
            
        } catch (Exception e) {
           
            request.setAttribute("mensagem", "Erro interno ao processar o cadastro. Verifique logs do banco.");
            request.setAttribute("tipoMensagem", "danger");
            request.getRequestDispatcher("/view/cadastro.jsp").forward(request, response);
        }
    }
}