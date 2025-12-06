

package br.com.bitpay.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import br.com.bitpay.util.ConnectionFactory;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class RecuperacaoSenhaService {

	 // ===============================
    // Gera código de 6 dígitos
    // ===============================
    private String gerarCodigo6Digitos() {
        int codigo = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(codigo);
    }

    // ===============================
    // Iniciar recuperação: salva código e envia e-mail
    // ===============================
    public String solicitarRecuperacaoSenha(String email) throws Exception {

        Connection conn = null;
        String call = "{ call PR_GERAR_TOKEN_RECUPERACAO(?, ?) }";

        try {
            conn = ConnectionFactory.getConnection();

            try (CallableStatement cstmt = conn.prepareCall(call)) {

                // entrada
                cstmt.setString(1, email);

                // saída (token gerado no Oracle)
                cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);

                cstmt.execute();

                // pega o código que o Oracle realmente salvou
                String codigo = cstmt.getString(2);

                // envia e-mail com o código correto
                enviarEmailRecuperacao(email, codigo);
                System.out.println("↳ Código gerado pelo Oracle: " + codigo);
                
                
                return codigo;
                
            }

        } catch (SQLException e) {
            throw new Exception("Falha ao solicitar recuperação: " + e.getMessage(), e);

        } finally {
            if (conn != null) conn.close();
        }
    }
    // ===============================
    // Confirmar código e alterar senha
    // ===============================
    
    public String buscarUltimoTokenDoUsuario(int idUsuario) throws Exception {
        String sql = """
            SELECT TOKEN
            FROM RECUPERACAO_SENHA
            WHERE ID_USUARIO = ?
            ORDER BY DATA_EXPIRACAO DESC
            FETCH FIRST 1 ROWS ONLY
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TOKEN");
                }
            }
        }
        return null;
    }

    public void recuperarSenha(String codigoDigitado, String novaSenha) throws Exception {

        // --- validação de null ---
        if (codigoDigitado == null || codigoDigitado.isEmpty()) {
            throw new Exception("O código informado é inválido ou está vazio.");
        }

        codigoDigitado = codigoDigitado.trim();
        String ultimoToken = null;

        // 1) Verifica token no BD
        String sql = """
            SELECT TOKEN
            FROM RECUPERACAO_SENHA
            WHERE TOKEN = ?
              AND USADO = 'N'
            ORDER BY DATA_EXPIRACAO DESC
            FETCH FIRST 1 ROW ONLY
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigoDigitado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ultimoToken = rs.getString("TOKEN");
                }
            }
        }

        if (ultimoToken == null) {
            throw new Exception("Código inválido ou já utilizado.");
        }

        // 2) Chama a procedure oficial
        String call = "{ call PR_RECUPERAR_SENHA(?, ?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {

            cstmt.setString(1, codigoDigitado);
            cstmt.setString(2, novaSenha);
            cstmt.execute();

        } catch (SQLException e) {
            throw new Exception("Falha ao recuperar senha: " + e.getMessage(), e);
        }
    }

    // -----------------------------------------
    // 3) ENVIO REAL DE E-MAIL (SMTP / MAILTRAP)
    // -----------------------------------------
    private void enviarEmailRecuperacao(String email, String codigo) {

        try {
            // ================================
            // CONFIGURAÇÃO SMTP DO GMAIL
            // ================================
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            "baroni.o@aluno.ifsp.edu.br", //gmail
                            "zxkn ltrt xbve yqdd" // senha de app
                    );
                }
            });

            // ================================
            // CRIA E-MAIL
            // ================================
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("no-reply@bitpaybank.com", "BitPay Bank"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setSubject("Código de Recuperação - BitPay Bank");

            // ================================
            // HTML DO E-MAIL
            // ================================
            String html = """
                <div style="font-family: Arial; padding: 20px; background: #f5f5f5;">
                    <div style="max-width: 480px; margin: auto; padding: 25px; background: white; border-radius: 8px;">
                        <h2 style="color: #333;">Recuperação de Senha</h2>
                        <p>Olá,</p>
                        <p>Seu código de recuperação é:</p>

                        <div style="font-size: 32px; font-weight: bold; background: #222; color: white; 
                                    padding: 14px; text-align: center; border-radius: 6px; letter-spacing: 4px;">
                            %s
                        </div>

                        <p style="margin-top: 15px;">O código expira em <b>10 minutos</b>.</p>
                        <p>Se você não solicitou a recuperação, ignore esta mensagem.</p>

                        <p style="color: #888; font-size: 12px; margin-top: 20px;">
                            BitPay Bank - Sistema de Recuperação de Conta
                        </p>
                    </div>
                </div>
            """.formatted(codigo);

            msg.setContent(html, "text/html; charset=UTF-8");

            // ================================
            // ENVIA E-MAIL
            // ================================
            Transport.send(msg);

            System.out.println("✔ Email REAL enviado com sucesso para: " + email);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar email: " + e.getMessage());
        }
    }

}