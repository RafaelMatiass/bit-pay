
--============================================
-- Consulta COM JOIN 
--============================================
SELECT C.ID AS C_ID, C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, 
      CO.NUMEROCONTA, CO.SALDO, CO.DATAABERTURA,
      E.VALORTOTAL, E.DATACONTRATACAO, E.NUMEROSPARCELAS, E.TAXAJUROS, 
      SE.DESCRICAO
      FROM CLIENTES C
      JOIN CONTAS CO ON C.ID = CO.IDCLIENTE
      JOIN EMPRESTIMOS E ON CO.ID = E.IDCONTA
      JOIN STATUSEMPRESTIMO SE ON E.IDSTATUSEMPRESTIMO = SE.ID;
      
----===========================================
--CONSULTA TODOS OS USUARIOS E SUAS CONTAS, ATÉ OS QUE NÃO TEM CONTAS
---============================================


SELECT  C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, 
        U.CPF, U.SENHA, U.EMAIL, U.IDTIPOUSUARIO,   
        CO.NUMEROCONTA, CO.SALDO, CO.DATAABERTURA
        FROM USUARIOS U
        LEFT JOIN CLIENTES C ON C.IDUSUARIO = U.ID
        LEFT JOIN CONTAS CO ON C.ID = CO.IDCLIENTE;
        
        
        
        SELECT U.id AS U_ID, U.CPF, U.SENHA, U.EMAIL, U.IDTIPOUSUARIO,
                   C.ID AS C_ID, C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, 
                   CO.NUMEROCONTA, CO.SALDO, CO.ID AS CO_ID, CO.IDSTATUSCONTA 
            FROM USUARIOS U 
            left JOIN CLIENTES C ON U.id = C.idUsuario 
            left JOIN CONTAS CO ON C.ID = CO.IDcliente;
            -- WHERE U.EMAIL = ? AND U.SENHA = ?";/
        