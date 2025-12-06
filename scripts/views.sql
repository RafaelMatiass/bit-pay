--============================================
-- Busca todos os clientes cadastrados com dados de usuario e conta
--============================================
CREATE VIEW CLIENTES_COMPLETO
AS  SELECT C.ID AS C_ID, C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, 
      U.id AS U_ID, U.CPF, U.SENHA, U.EMAIL, U.IDTIPOUSUARIO, 
      E.LOGRADOURO, E.NUMERO, E.COMPLEMENTO, E.BAIRRO, E.CIDADE, E.ESTADO, E.CEP,
      T.CODPAIS, T.CODAREA, T.NUMERO AS TELEFONE
     FROM CLIENTES C
     LEFT JOIN USUARIOS U ON U.id = C.idUsuario 
     LEFT JOIN ENDERECOS E ON C.IDENDERECO = E.ID
     LEFT JOIN TELEFONES T ON C.IDTELEFONE = T.ID;    
     
     SELECT * FROM CLIENTES_COMPLETO;
     
     
--==================================================
-- Buscar Todas as Clientes e Contas que possuem empréstimos e investimentos
--==================================================

CREATE VIEW CLIENTES_APLICACOES_VIEW
AS  SELECT C.ID AS C_ID, C.NOME, C.DATANASCIMENTO, C.DATACADASTRO, 
      CO.NUMEROCONTA, CO.SALDO, CO.DATAABERTURA,
      I.VALORAPLICADO,I.DATAAPLICACAO,TI.NOME AS INVESTIMENTO
     FROM CLIENTES C
     JOIN CONTAS CO ON C.id = CO.IDCLIENTE 
     JOIN APLICACOESINVESTIMENTOS I ON CO.ID = I.IDCONTA
     JOIN TIPOSINVESTIMENTO TI ON I.IDTIPOINVESTIMENTO = TI.ID;    
     
     SELECT * FROM CLIENTES_APLICACOES_VIEW;