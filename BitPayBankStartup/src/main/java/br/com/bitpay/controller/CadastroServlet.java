package br.com.bitpay.controller;

import br.com.bitpay.service.ContaService;
import br.com.bitpay.service.ContaServiceImpl;
import br.com.bitpay.service.EmailCadastroService;


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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // --------- CAPTURA E TRATA ESPAÇOS ---------
        String cpf             = trim(request.getParameter("cpf"));
        String email           = trim(request.getParameter("email"));
        String senha           = trim(request.getParameter("senha"));
        String confirmarSenha  = trim(request.getParameter("confirmarSenha"));
        String nome            = trim(request.getParameter("nome"));
        String dataNascimentoStr = trim(request.getParameter("dataNascimento"));
        String telefone        = trim(request.getParameter("telefone"));
        String cep             = trim(request.getParameter("cep"));
        String logradouro      = trim(request.getParameter("logradouro"));
        String numeroStr       = trim(request.getParameter("numero"));
        String bairro          = trim(request.getParameter("bairro"));
        String cidade          = trim(request.getParameter("cidade"));
        String estado          = trim(request.getParameter("estado"));

        // --------- VALIDAÇÕES DE CAMPOS BÁSICOS ---------
        // Campo vazio genérico
        if (isEmpty(cpf)) {
            setErro(request, "CPF é obrigatório.", "cpf");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(email)) {
            setErro(request, "E-mail é obrigatório.", "email");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(senha)) {
            setErro(request, "Senha é obrigatória.", "senha");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(confirmarSenha)) {
            setErro(request, "Confirmação de senha é obrigatória.", "confirmarSenha");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(nome)) {
            setErro(request, "Nome é obrigatório.", "nome");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(dataNascimentoStr)) {
            setErro(request, "Data de nascimento é obrigatória.", "dataNascimento");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(telefone)) {
            setErro(request, "Telefone é obrigatório.", "telefone");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(cep)) {
            setErro(request, "CEP é obrigatório.", "cep");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(logradouro)) {
            setErro(request, "Logradouro é obrigatório.", "logradouro");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(numeroStr)) {
            setErro(request, "Número é obrigatório.", "numero");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(bairro)) {
            setErro(request, "Bairro é obrigatório.", "bairro");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(cidade)) {
            setErro(request, "Cidade é obrigatória.", "cidade");
            forwardCadastro(request, response);
            return;
        }
        if (isEmpty(estado)) {
            setErro(request, "Estado (UF) é obrigatório.", "estado");
            forwardCadastro(request, response);
            return;
        }

        // --------- VALIDAÇÕES ESPECÍFICAS ---------

        // Senha x confirmação
        if (!senha.equals(confirmarSenha)) {
            setErro(request, "As senhas digitadas não coincidem.", "confirmarSenha");
            forwardCadastro(request, response);
            return;
        }

        // CPF: só números e 11 dígitos (validação simples)
        if (!cpf.matches("\\d{11}")) {
            setErro(request, "CPF inválido. Use apenas 11 números.", "cpf");
            forwardCadastro(request, response);
            return;
        }

        // CEP: só números e 8 dígitos
        if (!cep.matches("\\d{8}")) {
            setErro(request, "CEP inválido. Use apenas 8 números.", "cep");
            forwardCadastro(request, response);
            return;
        }

        // UF: 2 letras
        if (!estado.matches("(?i)[A-Z]{2}")) {
            setErro(request, "UF inválida. Use a sigla do estado, ex: SP, RJ, MG.", "estado");
            forwardCadastro(request, response);
            return;
        }

        // Data de nascimento
        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            setErro(request, "Formato de data de nascimento inválido.", "dataNascimento");
            forwardCadastro(request, response);
            return;
        }

        // Poderia validar idade mínima aqui (ex: 18 anos), se quiser depois

        // --------- CHAMADA DE NEGÓCIO ---------
        try {
            contaService.abrirConta(
                    cpf,
                    email,
                    senha,
                    nome,
                    dataNascimento,
                    telefone,
                    cep,
                    logradouro,
                    numeroStr,
                    bairro,
                    cidade,
                    estado
            );
            
            EmailCadastroService emailService = new EmailCadastroService();
            emailService.enviarEmailCadastroEmAnalise(email, nome);


            // Sucesso: mensagem na sessão e redireciona pro index
            request.getSession().setAttribute(
                    "mensagem",
                    "Cadastro enviado com sucesso! Sua conta está PENDENTE de aprovação gerencial. Aguarde..."
            );
            request.getSession().setAttribute("tipoMensagem", "success");

            // Melhor usar redirect para evitar reenvio de formulário
            response.sendRedirect(request.getContextPath() + "/index.jsp");

        } catch (IllegalArgumentException e) {
            // Erro de regra de negócio vindo do service
            request.setAttribute("mensagem", "Erro de negócio: " + e.getMessage());
            request.setAttribute("tipoMensagem", "danger");

            // Se quiser, você pode tentar mapear o campo pelo texto:
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("cpf")) {
                request.setAttribute("campoErro", "cpf");
            } else if (msg.contains("email")) {
                request.setAttribute("campoErro", "email");
            }

            forwardCadastro(request, response);

        } catch (Exception e) {
            e.printStackTrace(); // log
            request.setAttribute("mensagem", "Erro interno ao processar o cadastro. Tente novamente mais tarde.");
            request.setAttribute("tipoMensagem", "danger");
            forwardCadastro(request, response);
        }
    }

    // --------- HELPERS PRIVADOS ---------

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void setErro(HttpServletRequest request, String mensagem, String campoErro) {
        request.setAttribute("mensagem", mensagem);
        request.setAttribute("tipoMensagem", "danger");
        request.setAttribute("campoErro", campoErro); // id do input na JSP
    }

    private void forwardCadastro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/cadastro.jsp").forward(request, response);
    }
}
