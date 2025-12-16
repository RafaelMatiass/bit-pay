package br.com.bitpay.controller;

import br.com.bitpay.model.Cliente;
import br.com.bitpay.model.Emprestimo;
import br.com.bitpay.service.EmprestimoService;
import br.com.bitpay.model.Conta; 
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/meus-emprestimos", "/detalhes-emprestimo", "/pagar-parcela"})
public class GerenciarEmprestimosController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
   
    private Conta buscarContaCliente(HttpSession session) throws Exception {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null || cliente.getConta() == null) {
            throw new Exception("Dados de conta do cliente não encontrados na sessão.");
        }
        return cliente.getConta();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getServletPath();
        
        try {
            Conta conta = buscarContaCliente(session);
            int idConta = conta.getContaId();
            
            // 1. ROTA: /meus-emprestimos (Listar todos os empréstimos)
            if ("/meus-emprestimos".equals(action)) {
                List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosDoCliente(idConta);
                request.setAttribute("emprestimos", emprestimos);
                request.getRequestDispatcher("/view/meus_emprestimos.jsp").forward(request, response);
                return;
            }
            
            // 2. ROTA: /detalhes-emprestimo (Ver parcelas de um empréstimo)
            if ("/detalhes-emprestimo".equals(action)) {
                String idEmprestimoStr = request.getParameter("id");
                if (idEmprestimoStr == null) {
                     response.sendRedirect("meus-emprestimos");
                     return;
                }
                
                int idEmprestimo = Integer.parseInt(idEmprestimoStr);
                Emprestimo emprestimo = emprestimoService.detalharEmprestimo(idEmprestimo);
                
                if (emprestimo == null || emprestimo.getIdConta() != idConta) {
                    throw new IllegalArgumentException("Empréstimo não encontrado ou não pertence a esta conta.");
                }

                request.setAttribute("emprestimo", emprestimo);
                request.getRequestDispatcher("/view/detalhes_emprestimo.jsp").forward(request, response);
                return;
            }

        } catch (Exception e) {
            request.getSession().setAttribute("mensagemErro", "Erro ao carregar dados: " + e.getMessage());
            response.sendRedirect("meus-emprestimos"); 
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getServletPath();
        
        // 3. ROTA: /pagar-parcela (Processar pagamento)
        if ("/pagar-parcela".equals(action)) {
             try {
                Conta conta = buscarContaCliente(session);
                int idConta = conta.getContaId();
                
                String idParcelaStr = request.getParameter("idParcela");
                int idParcela = Integer.parseInt(idParcelaStr);

               
                emprestimoService.pagarParcela(idConta, idParcela);
                
                request.getSession().setAttribute("mensagemSucesso", "Parcela paga com sucesso!");
                
           
                String idEmprestimoStr = request.getParameter("idEmprestimo");
                response.sendRedirect("detalhes-emprestimo?id=" + idEmprestimoStr);
                return;

            }  catch (Exception e) {
                System.err.println("Erro no pagamento de parcela:");
                e.printStackTrace(); // 🔥 ESSENCIAL

                request.getSession().setAttribute(
                    "mensagemErro",
                    e.getCause() != null ? e.getCause().getMessage() : e.getMessage()
                );

                String idEmprestimoStr = request.getParameter("idEmprestimo");
                response.sendRedirect("detalhes-emprestimo?id=" + idEmprestimoStr);
                return;
            }

        }
        
      
        response.sendRedirect("meus-emprestimos");
    }
}