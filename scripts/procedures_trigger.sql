-- =========================================
-- Procedure: depositar
-- =========================================

CREATE OR REPLACE PROCEDURE PR_REALIZAR_DEPOSITO (
    p_id_conta      IN NUMBER,
    p_valor         IN NUMBER,
    p_id_tipo_mov   IN NUMBER DEFAULT 1 
)
AS
    v_valor_ajustado NUMBER := ABS(p_valor);
BEGIN

    INSERT INTO MOVIMENTACOES (id, idConta, valor, dataMovimento, idTipoMovimento)
    VALUES (seq_Movimentacoes.NEXTVAL, p_id_conta, v_valor_ajustado, SYSDATE, p_id_tipo_mov);

    COMMIT; 
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK; 
        RAISE_APPLICATION_ERROR(-20003, 'Erro na transação de depósito: ' || SQLERRM);
END;
/

-- Gatilho para Consistência de Saldo

CREATE OR REPLACE TRIGGER TRG_ATUALIZA_SALDO_CONTA
AFTER INSERT ON MOVIMENTACOES
FOR EACH ROW
DECLARE
    v_sinal NUMBER;
BEGIN
    CASE :NEW.IDTIPOMOVIMENTO
        WHEN 1 THEN -- DEPOSITO
            v_sinal := 1;
        WHEN 2 THEN -- SAQUE
            v_sinal := 1;
        WHEN 3 THEN -- TRANSFERENCIA ENVIADA
            v_sinal := 1;
        WHEN 4 THEN -- TRANSFERENCIA RECEBIDA
            v_sinal := 1;
        WHEN 5 THEN -- INVESTIMENTO
            v_sinal := 1;
        WHEN 6 THEN -- RESGATE
            v_sinal := 1;
        WHEN 7 THEN -- EMPRESTIMO
            v_sinal := 1;
        ELSE
            v_sinal := 0; -- Nenhuma alteração
    END CASE;

    -- Atualiza o saldo da conta
    UPDATE CONTAS
    SET SALDO = SALDO + (:NEW.VALOR * v_sinal)
    WHERE ID = :NEW.IDCONTA;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20005, 'Erro no gatilho de saldo para a conta ' || :NEW.IDCONTA || ': ' || SQLERRM);
END;
/


-- Controle de Transações (COMMIT/ROLLBACK)

CREATE OR REPLACE PROCEDURE PR_REALIZAR_TRANSFERENCIA (
    p_id_conta_origem   IN NUMBER,
    p_numero_destino    IN VARCHAR2,
    p_valor             IN NUMBER
)
AS
    v_id_conta_destino  CONTAS.ID%TYPE;
    v_saldo_origem      CONTAS.SALDO%TYPE;
    
    e_saldo_insuficiente EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_saldo_insuficiente, -20006); 

BEGIN
    -- 1. VALIDAÇÃO E BUSCA DE DADOS COM BLOQUEIO (FOR UPDATE)
    -- O lock impede que outra transação altere o saldo enquanto lemos/validamos
    SELECT SALDO INTO v_saldo_origem
    FROM CONTAS
    WHERE ID = p_id_conta_origem 
    FOR UPDATE; 
    
    -- Busca a conta destino pelo número e armazena o ID
    SELECT ID INTO v_id_conta_destino
    FROM CONTAS
    WHERE NUMEROCONTA = p_numero_destino;

    -- Checa se o saldo é suficiente
    IF v_saldo_origem < p_valor THEN
        RAISE e_saldo_insuficiente; 
    END IF;

    -- 2. DÉBITO (TRANSFER-OUT) - INSERT 1
    -- Rastreamento: IDCONTA_DESTINO = ID da conta de destino
    INSERT INTO MOVIMENTACOES (id, idConta, valor, dataMovimento, idTipoMovimento, idContaDestino)
    VALUES (seq_Movimentacoes.NEXTVAL, p_id_conta_origem, -ABS(p_valor), SYSDATE, 3, v_id_conta_destino); 

    -- 3. CRÉDITO (TRANSFER-IN) - INSERT 2
    -- Rastreamento: IDCONTA_DESTINO = ID da conta de origem (quem enviou)
    INSERT INTO MOVIMENTACOES (id, idConta, valor, dataMovimento, idTipoMovimento, idContaDestino)
    VALUES (seq_Movimentacoes.NEXTVAL, v_id_conta_destino, ABS(p_valor), SYSDATE, 4, p_id_conta_origem); 

    -- 4. CONFIRMAÇÃO (REQUISITO R6)
    COMMIT; 

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK; 
        RAISE_APPLICATION_ERROR(-20008, 'Erro na transação de transferência: ' || SQLERRM);
END;
/

-- =========================================
-- Procedure: token
-- =========================================

CREATE OR REPLACE PROCEDURE PR_GERAR_TOKEN_RECUPERACAO (
    p_email          IN  VARCHAR2,
    p_token_gerado   OUT VARCHAR2
)
AS
    v_id_usuario   NUMBER;
    v_token        VARCHAR2(6);
BEGIN
    -- 1) Buscar usuário
    SELECT ID INTO v_id_usuario
    FROM USUARIOS
    WHERE EMAIL = p_email;

    -- 2) Gerar código de 6 dígitos
    v_token := TO_CHAR(TRUNC(DBMS_RANDOM.VALUE(100000, 999999)));

    -- 3) Registrar código
    INSERT INTO RECUPERACAO_SENHA (
        ID, ID_USUARIO, TOKEN, DATA_EXPIRACAO, USADO
    ) VALUES (
        SEQ_RECUP_SENHA.NEXTVAL,
        v_id_usuario,
        v_token,
        SYSDATE + (10/1440), -- expira em 10 minutos
        'N'
    );

    -- 4) Retornar código gerado
    p_token_gerado := v_token;

    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20010, 'E-mail não encontrado.');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20011, 'Erro ao gerar código: ' || SQLERRM);
END;
/



-- =========================================
-- Procedure: Alterar senha
-- =========================================

CREATE OR REPLACE PROCEDURE PR_RECUPERAR_SENHA (
    p_token    IN VARCHAR2,
    p_senha    IN VARCHAR2
)
AS
    v_id_usuario NUMBER;
    v_exp        DATE;
BEGIN
    -- Validar token
    SELECT ID_USUARIO, DATA_EXPIRACAO
    INTO v_id_usuario, v_exp
    FROM RECUPERACAO_SENHA
    WHERE TRIM(TOKEN) = TRIM(p_token)
      AND USADO = 'N';

    -- Verificar expiração
    IF v_exp < SYSDATE THEN
        UPDATE RECUPERACAO_SENHA
        SET USADO = 'S'
        WHERE TRIM(TOKEN) = TRIM(p_token);

        RAISE_APPLICATION_ERROR(-20012, 'Token expirado.');
    END IF;

    -- Atualizar senha
    UPDATE USUARIOS
    SET SENHA = p_senha
    WHERE ID = v_id_usuario;

    -- Marcar token como utilizado
    UPDATE RECUPERACAO_SENHA
    SET USADO = 'S'
    WHERE TRIM(TOKEN) = TRIM(p_token);

    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20013, 'Token inválido.');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20014, 'Erro ao recuperar senha: ' || SQLERRM);
END;
/

-- PRODUCE PARA APROVAR CONTA 
CREATE OR REPLACE PROCEDURE PR_APROVAR_CONTA (
    p_id_conta IN NUMBER,
    p_id_status_aprovado IN NUMBER 
)
AS
    v_rows_updated NUMBER;
BEGIN
    UPDATE CONTAS
    SET IDSTATUSCONTA = p_id_status_aprovado
    WHERE ID = p_id_conta
      AND IDSTATUSCONTA != p_id_status_aprovado; 
      
    v_rows_updated := SQL%ROWCOUNT;

    IF v_rows_updated = 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Aprovação falhou: Conta ID ' || p_id_conta || ' não encontrada ou já aprovada.');
    END IF;
    
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Erro inesperado na aprovação da conta: ' || SQLERRM);
END;
/

-- INSERIR GERENTE
DECLARE
    v_id_usuario_gerado NUMBER;
    v_nome_gerente      VARCHAR2(150) := 'Marcelo Criscoulo';
    v_email_gerente     VARCHAR2(150) := 'marcelo@bitpay.com'; 
    v_senha_gerente     VARCHAR2(50)  := 'gerente123'; 
    v_data_nascimento   DATE          := DATE '1985-11-30'; 
    c_tipo_gerente      CONSTANT NUMBER := 2;
BEGIN
    -- PASSO 1: INSERIR USUARIO
    INSERT INTO USUARIOS (id, cpf, senha, email, idTipoUsuario)
    VALUES (seq_Usuarios.NEXTVAL, '00011122233', v_senha_gerente, v_email_gerente, c_tipo_gerente)
    RETURNING id INTO v_id_usuario_gerado; 
    
    -- PASSO 2: INSERIR NA TABELA GERENTES (Dados de Gerente)
    INSERT INTO GERENTES (id, nome, dataNascimento, idUsuario)
    VALUES (seq_Gerentes.NEXTVAL, v_nome_gerente, v_data_nascimento, v_id_usuario_gerado);

    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Gerente ' || v_nome_gerente || ' inserido com sucesso. ID Usuario: ' || v_id_usuario_gerado);
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Erro ao inserir gerente: ' || SQLERRM);
END;
/

-- TESTES
select * from usuarios;
select * from clientes;
select * from contas;
select * from statusconta;
select * from gerentes


