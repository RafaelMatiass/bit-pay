<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <base href="<%= request.getContextPath() %>/">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>BitPay - Banco Digital Futurista</title>

    <!-- Bootstrap para o modal -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        :root {
            --primary:#0d6efd;
            --primary-light:#e9f1ff;
            --bg:#f5f7fc;
            --text:#1c1f27;
            --card-bg:#ffffffcc;
            --radius:22px;
        }

        *{margin:0;padding:0;box-sizing:border-box;font-family:Inter, sans-serif;}

        body{
            background:var(--bg);
            color:var(--text);
            overflow-x:hidden;
        }

        /* -------- HEADER -------- */
        header{
            width:100%;
            padding:20px 8%;
            display:flex;
            justify-content:space-between;
            align-items:center;
            position:fixed;
            top:0;
            left:0;
            backdrop-filter:blur(10px);
            background:#ffffffaa;
            border-bottom:1px solid #ddd;
            z-index:30;
        }

        header h1{
            font-size:26px;
            color:var(--primary);
            font-weight:800;
            letter-spacing:-1px;
        }

        header nav a{
            margin-left:32px;
            text-decoration:none;
            color:var(--text);
            font-size:15px;
            font-weight:500;
            transition:.2s;
        }

        header nav a:hover{
            color:var(--primary);
        }

        .btn{
            padding:12px 28px;
            border-radius:var(--radius);
            border:none;
            font-size:15px;
            font-weight:600;
            cursor:pointer;
            transition:0.25s;
        }

        .btn-primary{
            background:var(--primary);
            color:white;
            box-shadow:0 8px 20px #0d6efd40;
        }

        .btn-primary:hover{
            transform:translateY(-3px);
            box-shadow:0 12px 26px #0d6efd50;
        }

        .btn-outline{
            background:transparent;
            border:2px solid var(--primary);
            color:var(--primary);
        }

        .btn-outline:hover{
            background:var(--primary-light);
        }

        /* -------- HERO -------- */
        .hero{
            padding-top:140px;
            display:flex;
            align-items:center;
            justify-content:space-between;
            padding-left:8%;
            padding-right:8%;
        }

        .hero-text h2{
            font-size:48px;
            font-weight:800;
            line-height:1.1;
            color:#0a2a57;
            margin-bottom:18px;
        }

        .hero-text p{
            font-size:17px;
            width:90%;
            margin-bottom:32px;
            opacity:.85;
        }

        .hero-graphics{
            width:480px;
            height:480px;
            background:linear-gradient(145deg,#0d6efd,#4c8dff);
            border-radius:50%;
            filter:blur(30px);
            opacity:.35;
            position:absolute;
            top:-60px;
            right:-120px;
            z-index:-1;
        }

        /* -------- SEÇÕES -------- */
        .section{
            padding:120px 8%;
        }

        .cards{
            display:grid;
            grid-template-columns:repeat(auto-fit,minmax(280px,1fr));
            gap:36px;
        }

        .card-custom{
            background:var(--card-bg);
            padding:32px;
            border-radius:var(--radius);
            box-shadow:0 10px 30px #0d6efd15;
            backdrop-filter:blur(10px);
            transition:.25s;
        }

        .card-custom:hover{
            transform:translateY(-8px);
            box-shadow:0 18px 42px #0d6efd25;
        }

        .card-custom h3{
            font-size:22px;
            margin-bottom:12px;
            color:#0a2a57;
        }

        .card-custom p{
            opacity:.8;
            font-size:15px;
        }

        /* -------- RENDIMENTOS -------- */
        .rendimentos{
            background:linear-gradient(180deg,#ffffff, #e8f0ff);
            padding:140px 8%;
        }

        .r-grid{
            display:grid;
            grid-template-columns:1fr 1fr;
            gap:60px;
            align-items:center;
        }

        .r-box{
            background:white;
            padding:40px;
            border-radius:var(--radius);
            box-shadow:0 10px 32px #0d6efd20;
        }

        .r-box h2{
            font-size:36px;
            font-weight:800;
            color:#0a2a57;
            margin-bottom:20px;
        }

        .r-item{
            display:flex;
            justify-content:space-between;
            margin-bottom:18px;
            font-size:17px;
            font-weight:600;
        }

        .r-item span:nth-child(2){
            color:var(--primary);
        }

        /* -------- CTA -------- */
        .cta{
            text-align:center;
            padding:120px 8%;
        }

        .cta h2{
            font-size:42px;
            margin-bottom:18px;
            color:#0a2a57;
        }

        .cta p{
            opacity:.8;
            margin-bottom:30px;
            font-size:17px;
        }

        /* -------- CARTÃO GIRANDO -------- */
        .investimento-giro{
            animation: girarPessoa 3s linear infinite;
            transform-origin:center bottom;
            transform-style:preserve-3d;
            perspective:1000px;
        }

        @keyframes girarPessoa{
            from{ transform: rotateY(0deg); }
            to  { transform: rotateY(360deg); }
        }

        /* -------- MODAL -------- */
        #loginModal{
            display:none;
            position:fixed;
            top:0; left:0;
            width:100%; height:100%;
            background:#0008;
            backdrop-filter:blur(6px);
            justify-content:center;
            align-items:center;
            z-index:999;
            padding:16px;
        }

        .modal-box{
            width:100%;
            max-width:420px;
            animation:fadeIn .25s;
        }

        @keyframes fadeIn{
            from{opacity:0; transform:scale(.94);}
            to{opacity:1; transform:scale(1);}
        }

        /* -------- FOOTER -------- */

        footer{
            background:#0d1117;
            padding:50px 8%;
            color:#c9d1d9;
            margin-top:120px;
        }

        .footer-grid{
            max-width:1200px;
            margin:auto;
            display:flex;
            flex-wrap:wrap;
            justify-content:space-between;
            gap:40px;
        }

        .footer-grid h4{
            font-size:16px;
            font-weight:700;
        }

        .footer-grid ul{
            list-style:none;
            padding:0;
            margin:0;
            line-height:1.9;
            font-size:14px;
        }

        .footer-grid a{
            color:#c9d1d9;
            text-decoration:none;
        }

        .footer-grid a:hover{
            color:#0d6efd;
        }

    </style>
</head>
<body>

<%
    String mensagem = (String) request.getAttribute("mensagem");
    String tipo = (String) request.getAttribute("tipoMensagem");
    boolean hasMensagem = mensagem != null && !mensagem.isEmpty();
    Object usuarioLogado = session.getAttribute("usuario");
%>

<%
    String mensagemSessao = (String) session.getAttribute("mensagem");
    String tipoSessao = (String) session.getAttribute("tipoMensagem");
    boolean mostrarModalAnalise = mensagemSessao != null && "success".equals(tipoSessao);
%>

<header>
    <h1>BitPay</h1>
    <nav>
        <a href="#sobre">Sobre</a>
        <a href="#seguranca">Segurança</a>
        <a href="#rendimento">Rendimentos</a>
    </nav>
    <button class="btn btn-outline">Entrar</button>
</header>

<div class="hero">
    <div class="hero-text">
        <h2>O Banco Azul do Futuro.</h2>
        <p>Segurança máxima, tecnologia de ponta e rendimentos superiores aos bancos tradicionais.</p>
        <button class="btn btn-primary" onclick="location.href='cadastro-page'">Abrir conta agora</button>
    </div>
</div>

<div class="hero-graphics"></div>

<section class="section" id="sobre">
    <h2 style="font-size:34px; margin-bottom:40px; color:#0a2a57; font-weight:800;">Por que escolher o BitPay?</h2>
    <div class="cards">
        <div class="card-custom">
            <h3>Tecnologia de ponta</h3>
            <p>Infraestrutura ultrarrápida com microsserviços e criptografia avançada.</p>
        </div>
        <div class="card-custom">
            <h3>Interface futurista</h3>
            <p>Experiência visual fluida e intuitiva, inspirada em bancos globais.</p>
        </div>
        <div class="card-custom">
            <h3>Atendimento 24/7</h3>
            <p>Suporte humano + IA disponível 24 horas.</p>
        </div>
    </div>
</section>

<section class="rendimentos" id="rendimento">
    <div class="r-grid">
        <img src="./utils/CreditCardBitPay.png" class="investimento-giro" style="width:100%;">
        <div class="r-box">
            <h2>Rendimentos Reais</h2>
            <div class="r-item"><span>Poupança Tech+</span><span>+12% a.a</span></div>
            <div class="r-item"><span>Bit CDI Turbo</span><span>220% do CDI</span></div>
            <div class="r-item"><span>Invest Seg XF</span><span>17% a.a</span></div>
            <div class="r-item"><span>AutoYield Smart</span><span>Rende todos os dias</span></div>
        </div>
    </div>
</section>

<section class="cta">
    <h2>Pronto para viver o futuro bancário?</h2>
    <p>Abra sua conta BitPay e experimente tecnologia + segurança como nunca antes.</p>
    <button class="btn btn-primary" onclick="location.href='cadastro-page'">Criar conta</button>
</section>


<!-- ---------------- FOOTER PREMIUM ---------------- -->

<footer>
    <div class="footer-grid">

        <div>
            <h2 style="color:#0d6efd; font-weight:800; font-size:26px;">BitPay Blue</h2>
            <p style="margin-top:10px; opacity:.8; max-width:280px;">
                Banco digital futurista, seguro e inteligente. Tecnologia de ponta ao alcance da sua mão.
            </p>
        </div>

        <div>
            <h4>Navegação</h4>
            <ul>
                <li><a href="#sobre">Sobre</a></li>
                <li><a href="#seguranca">Segurança</a></li>
                <li><a href="#rendimento">Rendimentos</a></li>
                <li><a href="cadastro-page" style="color:#0d6efd;">Abrir conta</a></li>
            </ul>
        </div>

        <div>
            <h4>Suporte</h4>
            <ul>
                <li><a href="#">Central de Ajuda</a></li>
                <li><a href="#">Atendimento 24h</a></li>
                <li><a href="view/recuperar-senha.jsp">Recuperar senha</a></li>
            </ul>
        </div>

        <div>
            <h4>Institucional</h4>
            <ul>
                <li><a href="#">Termos de Uso</a></li>
                <li><a href="#">Privacidade</a></li>
                <li><a href="#">Transparência</a></li>
            </ul>
        </div>

    </div>

    <hr style="border-color:#30363d; margin-top:40px;">

    <p style="text-align:center; margin-top:16px; font-size:13px; opacity:.6;">
        © 2025 BitPay Blue. Todos os direitos reservados.
    </p>
</footer>


<!-- ---------------- MODAL LOGIN ---------------- -->

<div id="loginModal" style="<%= hasMensagem ? "display:flex;" : "" %>">
    <div class="modal-box">
        <div class="card shadow border-0" style="border-radius:22px;">
            <div class="card-body">

                <h3 class="card-title text-center mb-1">Bit Pay Bank</h3>
                <p class="text-center text-muted mb-4">Acesse sua conta</p>
                
                <form action="login" method="POST">
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" required>
                    </div>

                    <div class="mb-2">
                        <label class="form-label">Senha</label>
                        <input type="password" class="form-control" name="senha" required>
                    </div>

                    <div class="text-end mb-3">
                        <a href="view/recuperar-senha.jsp" class="small text-primary">Esqueci minha senha</a>
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary">Entrar</button>
                    </div>
                </form>

                <% if(mensagem != null){ %>
                <div class="alert alert-<%= tipo != null ? tipo : "danger" %> mt-3">
                    <%= mensagem %>
                </div>
                <% } %>

                <button onclick="closeModal()" class="btn btn-link w-100 mt-3 text-muted text-decoration-none">
                    Fechar
                </button>
            </div>
        </div>
    </div>
</div>

<script>
function openModal(){ document.getElementById('loginModal').style.display = 'flex'; }
function closeModal(){ document.getElementById('loginModal').style.display = 'none'; }

document.querySelector('header .btn-outline').onclick =()=> openModal();
</script>

<!-- ================= MODAL CADASTRO EM ANÁLISE (ADICIONADO) ================= -->
<% if (mostrarModalAnalise) { %>
<div class="modal fade" id="modalCadastroAnalise" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Cadastro enviado para análise</h5>
            </div>
            <div class="modal-body">
                <p>Seu cadastro foi enviado com sucesso.</p>
                <p><strong>Status:</strong> Em análise interna.</p>
                <p>Você receberá um e-mail assim que a análise for concluída.</p>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" data-bs-dismiss="modal">Entendi</button>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
    new bootstrap.Modal(
        document.getElementById("modalCadastroAnalise")
    ).show();
});
</script>

<%
    session.removeAttribute("mensagem");
    session.removeAttribute("tipoMensagem");
%>
<% } %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
function openModal(){ document.getElementById('loginModal').style.display = 'flex'; }
function closeModal(){ document.getElementById('loginModal').style.display = 'none'; }

document.querySelector('header .btn-outline').onclick =()=> openModal();
</script>


</body>
</html>
