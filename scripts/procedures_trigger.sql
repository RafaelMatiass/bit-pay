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
BEGIN
    -- Atualiza o saldo da conta relacionada
    UPDATE CONTAS
    SET SALDO = SALDO +: NEW.VALOR
    WHERE ID =: NEW.IDCONTA;
    
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
    VALUES (seq_Movimentacoes.NEXTVAL, p_id_conta_origem, -ABS(p_valor), SYSDATE, 2, v_id_conta_destino); 

    -- 3. CRÉDITO (TRANSFER-IN) - INSERT 2
    -- Rastreamento: IDCONTA_DESTINO = ID da conta de origem (quem enviou)
    INSERT INTO MOVIMENTACOES (id, idConta, valor, dataMovimento, idTipoMovimento, idContaDestino)
    VALUES (seq_Movimentacoes.NEXTVAL, v_id_conta_destino, ABS(p_valor), SYSDATE, 3, p_id_conta_origem); 

    -- 4. CONFIRMAÇÃO (REQUISITO R6)
    COMMIT; 

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK; 
        RAISE_APPLICATION_ERROR(-20008, 'Erro na transação de transferência: ' || SQLERRM);
END;
/