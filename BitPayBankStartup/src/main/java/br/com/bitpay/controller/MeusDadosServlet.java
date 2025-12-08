package br.com.bitpay.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Endereco;
import br.com.bitpay.model.Telefone;
import br.com.bitpay.service.MeusDadosService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/meus-dados")
public class MeusDadosServlet extends HttpServlet {

    private MeusDadosService meusDadosService;

    @Override
    public void init() {
        meusDadosService = new MeusDadosService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Exibe a página — usa o JSP em /view/meus-dados.jsp
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        req.getRequestDispatcher("/view/meus-dados.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Cliente armazenado na sessão (assumimos que login já colocou o Cliente)
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");

        // Campos do formulário
        cliente.setNome(req.getParameter("nome"));
        cliente.setEmail(req.getParameter("email"));

        String dataNascimentoStr = req.getParameter("dataNascimento");
        if (dataNascimentoStr != null && !dataNascimentoStr.isBlank()) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            cliente.setDataNascimento(LocalDate.parse(dataNascimentoStr, fmt));
        }

        // Endereco
        if (cliente.getEndereco() == null) cliente.setEndereco(new Endereco());
        cliente.getEndereco().setLogradouro(req.getParameter("logradouro"));
        String numeroStr = req.getParameter("numero");
        try { cliente.getEndereco().setNumero(numeroStr != null && !numeroStr.isBlank() ? Integer.parseInt(numeroStr) : 0); }
        catch (NumberFormatException ex) { cliente.getEndereco().setNumero(0); }
        cliente.getEndereco().setComplemento(req.getParameter("complemento"));
        cliente.getEndereco().setBairro(req.getParameter("bairro"));
        cliente.getEndereco().setCidade(req.getParameter("cidade"));
        cliente.getEndereco().setEstado(req.getParameter("estado"));
        cliente.getEndereco().setCep(req.getParameter("cep"));

        // Telefone
        if (cliente.getTelefone() == null) cliente.setTelefone(new Telefone());
        try { cliente.getTelefone().setCodArea(Integer.parseInt(req.getParameter("ddd"))); } catch (Exception ex) {}
        try { cliente.getTelefone().setNumero(req.getParameter("telefone") != null && !req.getParameter("telefone").isBlank() ? Long.parseLong(req.getParameter("telefone")) : 0L); } catch (Exception ex) {}
        // codPais opcional
        try { String cp = req.getParameter("codPais"); if (cp != null && !cp.isBlank()) cliente.getTelefone().setCodPais(Integer.parseInt(cp)); } catch (Exception ex) {}

        // Toggle: alterarSenha (valor "on" ou "true" caso ativado)
        String alterarSenhaParam = req.getParameter("alterarSenha");
        boolean alterarSenha = "on".equalsIgnoreCase(alterarSenhaParam) || "true".equalsIgnoreCase(alterarSenhaParam) || "1".equals(alterarSenhaParam);

        if (alterarSenha) {
            String senhaAtual = req.getParameter("senhaAtual");
            String novaSenha = req.getParameter("novaSenha");
            String confirmarSenha = req.getParameter("confirmarSenha");

            // validações básicas
            if (senhaAtual == null || senhaAtual.isBlank()) {
                req.setAttribute("erro", "Informe a senha atual.");
                req.getRequestDispatcher("/view/meus-dados.jsp").forward(req, resp);
                return;
            }
            if (!cliente.getSenha().equals(senhaAtual)) {
                req.setAttribute("erro", "Senha atual incorreta.");
                req.getRequestDispatcher("/view/meus-dados.jsp").forward(req, resp);
                return;
            }
            if (novaSenha == null || novaSenha.isBlank() || confirmarSenha == null || confirmarSenha.isBlank() || !novaSenha.equals(confirmarSenha)) {
                req.setAttribute("erro", "Nova senha vazia ou confirmação diferente.");
                req.getRequestDispatcher("/view/meus-dados.jsp").forward(req, resp);
                return;
            }
            // prepara nova senha no objeto cliente
            cliente.setSenha(novaSenha);
        } else {
            // não alterar senha: manter a senha atual (já está em cliente.getSenha())
            // Não mudar cliente.setSenha()
        }

        // chama service transacional
        try {
            meusDadosService.atualizarDadosCompletos(cliente, alterarSenha);
            // atualiza sessão com dados novos
            session.setAttribute("usuarioLogado", cliente);
            req.setAttribute("sucesso", "Dados atualizados com sucesso.");
            req.getRequestDispatcher("/view/meus-dados.jsp").forward(req, resp);
        } catch (Exception e) {
            // log (em produção use logger)
            e.printStackTrace();
            throw new ServletException("Erro ao atualizar dados do usuário", e);
        }
    }
}
