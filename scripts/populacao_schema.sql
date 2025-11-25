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
