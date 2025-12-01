package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Gerente;
import br.com.bitpay.model.Usuario;
import br.com.bitpay.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private LoginService loginService = new LoginService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
        	Usuario usuarioLogado = loginService.autenticar(email, senha);
            
            HttpSession session = request.getSession(true); 
            
            session.removeAttribute("cliente"); 
            
            session.setAttribute("usuarioLogado", usuarioLogado);
            
            
            if (usuarioLogado instanceof Gerente) {
                response.sendRedirect(request.getContextPath() + "/gerente-dashboard"); 
                return;
                
            } else if (usuarioLogado instanceof Cliente) {
                Cliente clienteLogado = (Cliente) usuarioLogado;
                session.setAttribute("conta", clienteLogado.getConta()); 
                
                if (clienteLogado.getConta() != null && clienteLogado.getConta().getStatusConta() != null && clienteLogado.getConta().getStatusConta().getCodigo() == 2) {
                    response.sendRedirect(request.getContextPath() + "/home");
                    return;
                } else {
                    request.setAttribute("mensagem", "Sua conta está em análise ou aguardando aprovação.");
                    request.setAttribute("tipoMensagem", "warning"); 
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }
            } else {
                throw new Exception("Perfil de usuário inválido ou não suportado.");
            }
            
        } catch (Exception e) {
            request.setAttribute("mensagem", e.getMessage());
            request.setAttribute("tipoMensagem", "danger"); 
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}