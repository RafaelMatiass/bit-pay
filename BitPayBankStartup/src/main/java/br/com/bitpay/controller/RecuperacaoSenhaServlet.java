package br.com.bitpay.controller;

import java.io.IOException;

import br.com.bitpay.service.RecuperacaoSenhaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/recuperar-senha")
public class RecuperacaoSenhaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private RecuperacaoSenhaService service = new RecuperacaoSenhaService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        try {
            service.solicitarRecuperacaoSenha(email);
            req.setAttribute("msg", "Código enviado para seu e-mail.");
            req.getRequestDispatcher("view/confirmar-token.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("view/recuperar-senha.jsp").forward(req, resp);
        }
    }
}