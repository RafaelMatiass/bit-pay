package br.com.bitpay.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet ("/cadastro-page")
public class CadastroPageServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	
	// Tem a função apenas de tratar o evento disparado quando o usuário clica em 
	// abrir conta na página login. No caso, direciona para a página de cadastro
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		request.getRequestDispatcher("/view/cadastro.jsp").forward(request, response);
	}

}
