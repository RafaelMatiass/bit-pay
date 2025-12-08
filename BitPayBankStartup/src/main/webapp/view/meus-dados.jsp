<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>



<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <title>Meus Dados - BitPay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background: #f4f9ff;
        }

        .card {
            border-radius: 14px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.07);
        }

        .section-title {
            color: #0d6efd;
            font-weight: 600;
            margin-bottom: 15px;
        }

        .toggle-container {
            cursor: pointer;
            font-weight: 600;
        }

        #senhaBox {
            display: none;
        }
    </style>
</head>

<body>

<div class="container mt-4 mb-5">

    <h2 class="text-center mb-4" style="color:#0d6efd;">Meus Dados</h2>

    <!-- Exibe mensagens -->
    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <c:if test="${not empty sucesso}">
        <div class="alert alert-success">${sucesso}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/meus-dados" method="post">

        <div class="card p-4 mb-4">
            <h5 class="section-title">Informações Pessoais</h5>

            <div class="row">
                <div class="col-md-6">
                    <label class="form-label">Nome Completo</label>
                    <input type="text" class="form-control" name="nome" value="${usuarioLogado.nome}" required>
                </div>

                <div class="col-md-6">
                    <label class="form-label">Data de Nascimento</label>
                    <input type="date" class="form-control" name="dataNascimento"
                           value="${usuarioLogado.dataNascimento}">
                </div>

                <div class="col-md-6 mt-3">
                    <label class="form-label">E-mail</label>
                    <input type="email" class="form-control" name="email" value="${usuarioLogado.email}" required>
                </div>
            </div>
        </div>


        <!-- Endereço -->
        <div class="card p-4 mb-4">
            <h5 class="section-title">Endereço</h5>

            <div class="row">
                <div class="col-md-8">
                    <label class="form-label">Logradouro</label>
                    <input type="text" class="form-control" name="logradouro"
                           value="${usuarioLogado.endereco.logradouro}">
                </div>

                <div class="col-md-4">
                    <label class="form-label">Número</label>
                    <input type="text" class="form-control" name="numero"
                           value="${usuarioLogado.endereco.numero}">
                </div>

                <div class="col-md-4 mt-3">
                    <label class="form-label">Bairro</label>
                    <input type="text" class="form-control" name="bairro"
                           value="${usuarioLogado.endereco.bairro}">
                </div>

                <div class="col-md-4 mt-3">
                    <label class="form-label">Cidade</label>
                    <input type="text" class="form-control" name="cidade"
                           value="${usuarioLogado.endereco.cidade}">
                </div>

                <div class="col-md-2 mt-3">
                    <label class="form-label">Estado</label>
                    <input type="text" maxlength="2" class="form-control" name="estado"
                           value="${usuarioLogado.endereco.estado}">
                </div>

                <div class="col-md-2 mt-3">
                    <label class="form-label">CEP</label>
                    <input type="text" class="form-control" name="cep"
                           value="${usuarioLogado.endereco.cep}">
                </div>
            </div>
        </div>


        <!-- Telefone -->
        <div class="card p-4 mb-4">
            <h5 class="section-title">Telefone</h5>

            <div class="row">
                <div class="col-md-2">
                    <label class="form-label">Código País</label>
                    <input type="text" class="form-control" name="codPais"
                           value="${usuarioLogado.telefone.codPais}">
                </div>

                <div class="col-md-2">
                    <label class="form-label">DDD</label>
                    <input type="text" class="form-control" name="ddd"
                           value="${usuarioLogado.telefone.codArea}">
                </div>

                <div class="col-md-4">
                    <label class="form-label">Número</label>
                    <input type="text" class="form-control" name="telefone"
                           value="${usuarioLogado.telefone.numero}">
                </div>
            </div>
        </div>


        <!-- Alterar Senha -->
        <div class="card p-4 mb-4">
            <h5 class="section-title">Segurança</h5>

            <div class="form-check form-switch mb-3">
                <input class="form-check-input" type="checkbox" id="alterarSenha" name="alterarSenha">
                <label class="form-check-label" for="alterarSenha">Quero alterar minha senha</label>
            </div>

            <div id="senhaBox">
                <div class="row">
                    <div class="col-md-4">
                        <label class="form-label">Senha Atual</label>
                        <input type="password" class="form-control" name="senhaAtual">
                    </div>

                    <div class="col-md-4">
                        <label class="form-label">Nova Senha</label>
                        <input type="password" class="form-control" name="novaSenha">
                    </div>

                    <div class="col-md-4">
                        <label class="form-label">Confirmar Nova Senha</label>
                        <input type="password" class="form-control" name="confirmarSenha">
                    </div>
                </div>
            </div>
        </div>


        <!-- Botão -->
        <div class="text-center">
            <button class="btn btn-primary px-5 py-2" style="font-size:1.2rem;">Salvar Alterações</button>
        </div>

    </form>

</div>

<script>
    document.getElementById("alterarSenha").addEventListener("change", function () {
        document.getElementById("senhaBox").style.display = this.checked ? "block" : "none";
    });
</script>

</body>
</html>
