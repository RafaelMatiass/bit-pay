package br.com.bitpay.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Conta;
import br.com.bitpay.model.Movimentacao;
import br.com.bitpay.service.ExtratoService;
import br.com.bitpay.service.ExtratoServiceImpl;
import br.com.bitpay.util.PdfGenerator;

@WebServlet("/extrato")
public class ExtratoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ExtratoService extratoService = new ExtratoServiceImpl();

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Conta conta = (Conta) session.getAttribute("conta"); 
        
        Object usuarioLogado = (session != null) ? session.getAttribute("usuarioLogado") : null;

        if (usuarioLogado == null || !(usuarioLogado instanceof Cliente)) {
            response.sendRedirect("login"); 
            return;
        }

        LocalDate dataFinal = LocalDate.now();
        LocalDate dataInicial = dataFinal.minusDays(30);

        List<Movimentacao> extrato = extratoService.gerarExtrato(conta.getContaId(), dataInicial, dataFinal);

        request.setAttribute("extrato", extrato);
        request.setAttribute("dataInicial", dataInicial.toString()); 
        request.setAttribute("dataFinal", dataFinal.toString());

        request.getRequestDispatcher("/view/extrato.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Conta conta = (Conta) session.getAttribute("conta"); 
        if (conta == null) {
            response.sendRedirect("login"); 
            return;
        }

        String dataInicialStr = request.getParameter("dataInicial");
        String dataFinalStr = request.getParameter("dataFinal");
        String acao = request.getParameter("acao"); 

        LocalDate dataInicial = null;
        LocalDate dataFinal = null;

        try {
            dataInicial = LocalDate.parse(dataInicialStr);
            dataFinal = LocalDate.parse(dataFinalStr);
        } catch (DateTimeParseException e) {
            request.setAttribute("erro", "Formato de data inválido.");
            request.getRequestDispatcher("/view/extrato.jsp").forward(request, response);
            return;
        }

        List<Movimentacao> extrato = extratoService.gerarExtrato(conta.getContaId(), dataInicial, dataFinal);

        if ("gerarPDF".equals(acao)) {
        	gerarPdf(response, conta, extrato, dataInicial, dataFinal);
        } else {
        	request.setAttribute("extrato", extrato);
            request.setAttribute("dataInicial", dataInicialStr);
            request.setAttribute("dataFinal", dataFinalStr);
            
            request.setAttribute("filterSubmitted", true);
            
            request.getRequestDispatcher("/view/extrato.jsp").forward(request, response);
        }
	}
    
    private void gerarPdf(HttpServletResponse response, Conta conta, List<Movimentacao> extrato, LocalDate dataInicial, LocalDate dataFinal) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"extrato_" + dataInicial + "_a_" + dataFinal + ".pdf\"");
        
        try (OutputStream out = response.getOutputStream()) {
        	PdfGenerator.gerarExtratoPdf(out, conta, extrato, dataInicial, dataFinal);
        	} catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao gerar PDF.");
        }
    }
}