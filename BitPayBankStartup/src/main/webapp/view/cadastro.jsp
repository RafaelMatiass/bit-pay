<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Bit Pay - Abrir Conta</title>
    <base href="<%= request.getContextPath() %>/">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        :root {
            --primary:#0d6efd;
            --primary-light:#e9f1ff;
            --bg:#f5f7fc;
            --text:#1c1f27;
            --card-bg:#ffffffee;
            --radius:22px;
        }

        *{margin:0;padding:0;box-sizing:border-box;font-family:Inter, system-ui, sans-serif;}

        body{
            min-height:100vh;
            background:radial-gradient(circle at top left,#4c8dff33,#0d6efd11 40%,#0a1a33 100%), var(--bg);
            color:var(--text);
            display:flex;
            align-items:center;
            justify-content:center;
            padding:24px;
        }

        .auth-wrapper{
            width:100%;
            max-width:1050px;
        }

        .brand-pill{
            display:inline-flex;
            align-items:center;
            gap:8px;
            padding:4px 14px;
            border-radius:999px;
            background:#0d6efd12;
            color:#0d6efd;
            font-size:12px;
            font-weight:600;
            text-transform:uppercase;
            letter-spacing:.05em;
        }

        .card-modern{
            border-radius:var(--radius);
            border:1px solid #0d6efd25;
            box-shadow:0 18px 60px #0d6efd33;
            background:linear-gradient(135deg,#ffffff,#eff4ff);
            overflow:hidden;
        }

        /* ------- LATERAL ESQUERDA --------- */

        .side-panel{
            background:radial-gradient(circle at top,#0d6efd,#001b4d);
            color:white;
            padding:40px 32px;
            height:100%;
        }

        .side-panel h2{
            font-size:28px;
            font-weight:900;
            margin-bottom:10px;
            letter-spacing:-0.5px;
        }

        .side-panel p{
            font-size:14px;
            opacity:.9;
        }

        .badge-feature{
            display:flex;
            align-items:center;
            gap:10px;
            margin-top:14px;
            font-size:13px;
            opacity:.9;
        }

        .dot{
            width:8px;
            height:8px;
            border-radius:50%;
            background:#4cffd2;
            box-shadow:0 0 8px #4cffd2;
        }

        .info-box{
            background:rgba(255,255,255,0.08);
            padding:14px 16px;
            border-radius:12px;
            border:1px solid rgba(255,255,255,0.2);
            margin:25px 0;
        }

        .info-title{
            font-size:14px;
            font-weight:700;
            color:#4cffd2;
            display:block;
            margin-bottom:6px;
        }

        .info-text{
            font-size:13px;
            opacity:.85;
        }

        ul.benefits{
            padding-left:18px;
            font-size:13px;
            opacity:.9;
            line-height:1.5;
        }

        /* --------- CAMPOS ----------- */

        .form-title{
            font-size:24px;
            font-weight:800;
            color:#0a2a57;
        }

        .section-title{
            font-size:13px;
            font-weight:800;
            letter-spacing:.08em;
            color:#7b8094;
        }

        .section-divider{
            border-top:1px dashed #c8d0e8;
            margin:10px 0 18px;
        }

        .form-control{
            border-radius:14px;
            border:1px solid #ccd3ea;
            font-size:14px;
            padding:9px 11px;
        }

        .form-control:focus{
            border-color:var(--primary);
            box-shadow:0 0 0 0.16rem #0d6efd33;
        }

        .is-invalid{
            border-color:#dc3545 !important;
            box-shadow:0 0 0 0.16rem #dc354555 !important;
        }

        /* -------- BOTÕES ---------- */

        .btn-primary{
            background:var(--primary);
            border:none;
            border-radius:16px;
            font-weight:600;
            padding:12px 0;
            box-shadow:0 10px 25px #0d6efd55;
        }

        .btn-primary:hover{
            filter:brightness(1.05);
            transform:translateY(-1px);
        }

        .btn-outline-secondary{
            border-radius:16px;
            padding:12px 0;
        }

        /* Responsividade */
        @media (max-width: 768px){
            .side-panel{
                display:none;
            }
        }
    </style>
</head>
<body>

<%
    String mensagem = (String) request.getAttribute("mensagem");
    String tipo = (String) request.getAttribute("tipoMensagem");
    String campoErro = (String) request.getAttribute("campoErro");
%>

<div class="auth-wrapper">

    <div class="mb-3 text-center">
        <span class="brand-pill">Bit Pay • Abertura de Conta</span>
    </div>

    <div class="card card-modern">
        <div class="row g-0">

            <!-- ------------------------------------------ -->
            <!--               LATERAL ESQUERDA            -->
            <!-- ------------------------------------------ -->

            <div class="col-md-4 side-panel">

                <h2>Bit Pay Blue</h2>

                <p>
                    Sua nova conta digital premium, com foco em tecnologia,
                    segurança e análise inteligente de crédito.
                </p>

                <div class="info-box">
                    <span class="info-title">Tecnologia de ponta</span>
                    <p class="info-text">
                        Algoritmos avançados analisam seu perfil para oferecer crédito justo
                        e evolução automática de limite.
                    </p>
                </div>

                <div class="badge-feature">
                    <span class="dot"></span>
                    <span>Crédito sujeito à análise</span>
                </div>

                <div class="badge-feature">
                    <span class="dot"></span>
                    <span>Conta gratuita e 100% digital</span>
                </div>

                <div class="badge-feature">
                    <span class="dot"></span>
                    <span>Atendimento humano 24h</span>
                </div>

                <hr style="border-color:#ffffff33; margin:28px 0;">

                <p class="small mb-2" style="opacity:.85;">Vantagens exclusivas Bit Pay Blue:</p>

                <ul class="benefits">
                    <li>Limite inicial inteligente</li>
                    <li>Investimentos integrados</li>
                    <li>Dashboard financeiro com IA</li>
                    <li>Design premium com foco em UX</li>
                </ul>
            </div>

            <!-- ------------------------------------------ -->
            <!--               FORMULÁRIO DIREITA           -->
            <!-- ------------------------------------------ -->

            <div class="col-md-8">
                <div class="card-body p-4 p-md-5">

                    <h3 class="form-title mb-1">Abra sua conta Bit Pay</h3>
                    <p class="text-muted mb-4">
                        Seus dados serão enviados para análise de crédito de forma segura.
                    </p>

                    <form action="cadastro" method="POST">

                        <!-- DADOS DE ACESSO -->
                        <span class="section-title">Dados de acesso</span>
                        <div class="section-divider"></div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="cpf" class="form-label">CPF</label>
                                <input 
                                    type="text"
                                    maxlength="11"
                                    id="cpf"
                                    name="cpf"
                                    class="form-control <%= "cpf".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("cpf") != null ? request.getParameter("cpf") : "" %>"
                                    required>
                            </div>

                            <div class="col-md-6">
                                <label for="email" class="form-label">E-mail</label>
                                <input 
                                    type="email"
                                    id="email"
                                    name="email"
                                    class="form-control <%= "email".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
                                    required>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="senha" class="form-label">Senha</label>
                                <input 
                                    type="password"
                                    id="senha"
                                    name="senha"
                                    class="form-control <%= "senha".equals(campoErro) ? "is-invalid" : "" %>"
                                    required>
                            </div>

                            <div class="col-md-6">
                                <label for="confirmarSenha" class="form-label">Confirmar senha</label>
                                <input 
                                    type="password"
                                    id="confirmarSenha"
                                    name="confirmarSenha"
                                    class="form-control <%= "confirmarSenha".equals(campoErro) ? "is-invalid" : "" %>"
                                    required>
                            </div>
                        </div>

                        <!-- DADOS PESSOAIS -->
                        <span class="section-title">Dados pessoais</span>
                        <div class="section-divider"></div>

                        <div class="row mb-3">
                            <div class="col-md-8">
                                <label for="nome" class="form-label">Nome completo</label>
                                <input 
                                    type="text"
                                    id="nome"
                                    name="nome"
                                    class="form-control <%= "nome".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("nome") != null ? request.getParameter("nome") : "" %>"
                                    required>
                            </div>

                            <div class="col-md-4">
                                <label for="dataNascimento" class="form-label">Data de nascimento</label>
                                <input 
                                    type="date"
                                    id="dataNascimento"
                                    name="dataNascimento"
                                    class="form-control <%= "dataNascimento".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("dataNascimento") != null ? request.getParameter("dataNascimento") : "" %>"
                                    required>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="telefone" class="form-label">Telefone</label>
                                <input 
                                    type="text"
                                    id="telefone"
                                    name="telefone"
                                    class="form-control <%= "telefone".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("telefone") != null ? request.getParameter("telefone") : "" %>"
                                    required>
                            </div>

                            <div class="col-md-6">
                                <label for="cep" class="form-label">CEP</label>
                                <input 
                                    type="text"
                                    maxlength="8"
                                    id="cep"
                                    name="cep"
                                    class="form-control <%= "cep".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("cep") != null ? request.getParameter("cep") : "" %>"
                                    required>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-9">
                                <label for="logradouro" class="form-label">Logradouro</label>
                                <input 
                                    type="text"
                                    id="logradouro"
                                    name="logradouro"
                                    class="form-control <%= "logradouro".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("logradouro") != null ? request.getParameter("logradouro") : "" %>"
                                    required>
                            </div>

                            <div class="col-md-3">
                                <label for="numero" class="form-label">Número</label>
                                <input 
                                    type="text"
                                    id="numero"
                                    name="numero"
                                    class="form-control <%= "numero".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("numero") != null ? request.getParameter("numero") : "" %>"
                                    required>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label for="bairro" class="form-label">Bairro</label>
                                <input 
                                    type="text"
                                    id="bairro"
                                    name="bairro"
                                    class="form-control <%= "bairro".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("bairro") != null ? request.getParameter("bairro") : "" %>"
                                    required>
                            </div>

                            <div class="col-md-4">
                                <label for="cidade" class="form-label">Cidade</label>
                                <input 
                                    type="text"
                                    id="cidade"
                                    name="cidade"
                                    class="form-control <%= "cidade".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("cidade") != null ? request.getParameter("cidade") : "" %>"
                                    required>
                            </div>

                            <div class="col-md-4">
                                <label for="estado" class="form-label">UF</label>
                                <input 
                                    type="text"
                                    maxlength="2"
                                    id="estado"
                                    name="estado"
                                    class="form-control <%= "estado".equals(campoErro) ? "is-invalid" : "" %>"
                                    value="<%= request.getParameter("estado") != null ? request.getParameter("estado") : "" %>"
                                    required>
                            </div>
                        </div>

                        <div class="d-grid gap-2 mt-4">
                            <button type="submit" class="btn btn-primary btn-lg">
                                Abrir minha conta
                            </button>
                            <a href="index.jsp" class="btn btn-outline-secondary">
                                Voltar para o início
                            </a>
                        </div>

                    </form>

                    <% if (mensagem != null) { %>
                        <div class="alert alert-<%= tipo != null ? tipo : "danger" %> mt-3">
                            <%= mensagem %>
                        </div>
                    <% } %>

                    <p class="help-text text-center mt-3">
                        Ao continuar, você concorda com os termos de uso e política de privacidade Bit Pay.
                    </p>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
(function(){
    var campo = "<%= campoErro != null ? campoErro : "" %>";
    if(campo){
        let el = document.getElementById(campo);
        if(el){
            el.focus();
            el.select?.();
        }
    }
})();
</script>

</body>
</html>
