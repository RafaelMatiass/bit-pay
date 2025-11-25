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