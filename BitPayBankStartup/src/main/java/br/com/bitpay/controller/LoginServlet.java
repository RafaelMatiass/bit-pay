package br.com.bitpay.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		// Esse servlet será responsável por validar os dados de login e criar a sessão. 
		//Para teste, neste momento está apenas criando uma sessão fake e redirecionando para o home
		HttpSession sessao = request.getSession();
		
		sessao.setAttribute("idUsuarioLogado", 1L);
		response.sendRedirect("home");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		
		response.sendRedirect("index.jsp");
	}

}
