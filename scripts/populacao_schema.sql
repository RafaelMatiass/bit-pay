INSERT INTO TiposUsuario (id, tipo)
VALUES
(1, 'CLIENTE'),
(2, 'GERENTE');
SELECT * FROM TiposUsuario;


INSERT INTO StatusConta (id, nomeStatus)
VALUES
(1, 'PENDENTE'),
(2, 'ATIVA'),
(3, 'INATIVA');
SELECT * FROM StatusConta;

INSERT INTO TiposMovimento (id, tipoMovimento)
VALUES
(1, 'DEPOSITO'),
(2, 'SAQUE'),
(3, 'TRANSFERENCIA ENVIADA'),
(4, 'TRANSFERENCIA RECEBIDA'),
(5, 'INVESTIMENTO');

SELECT * FROM TiposMovimento;


-- Inserindo algumas possibilidades de investimentos para teste
INSERT INTO TiposInvestimento ( id ,nome, rentabilidadeMes, carenciaDias, valorMinimo) 
VALUES (seq_TiposInvestimento .NEXTVAL,'Poupança BitPay', 0.005000, 0, 10.00); -- 0.5% ao mês, sem carência

INSERT INTO TiposInvestimento ( id ,nome, rentabilidadeMes, carenciaDias, valorMinimo) 
VALUES (seq_TiposInvestimento .NEXTVAL,'CDB Premium 2025', 0.012000, 30, 100.00); -- 1.2% ao mês, 30 dias carência

INSERT INTO TiposInvestimento (id ,nome, rentabilidadeMes, carenciaDias, valorMinimo) 
VALUES (seq_TiposInvestimento .NEXTVAL,'Fundo Tech High Yield', 0.025000, 180, 1000.00); -- 2.5% ao mês, 180 dias carência
SELECT * FROM TiposInvestimento;



