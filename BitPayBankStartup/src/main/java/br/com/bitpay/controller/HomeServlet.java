package br.com.bitpay.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sessao = request.getSession(false);
		
		if(sessao== null || sessao.getAttribute("idUsuarioLogado") == null ) {
			 response.sendRedirect("login.jsp");
			 return;
		}
		
	
		
		// aqui devemos chamar os métodos do DAO que acessa o banco e traz os dados do usuário com
		// o id da sessão e preenche a página home com esses dados
		
		String nomeUsuario = "Lucas (Usuário de Teste)";
        double saldoConta = 1500.75;
        
       
        request.setAttribute("nomeUsuario", nomeUsuario);
        request.setAttribute("saldoConta", saldoConta);
        
        request.getRequestDispatcher("/view/home.jsp").forward(request, response);
		
	}

}
