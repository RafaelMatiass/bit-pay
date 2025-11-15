-- executar no system ou sys

ALTER SESSION SET CONTAINER = FREEPDB1;

-- usuario responsável pelos scripts
CREATE USER bitpay_schema IDENTIFIED BY senha123;

-- Dar as permissões básicas para ele criar o banco
GRANT CONNECT, RESOURCE, CREATE VIEW TO bitpay_schema;
GRANT UNLIMITED TABLESPACE TO bitpay_schema;

-- ROLE para o aplicativo que vai consumir os dados
CREATE ROLE ROLE_APP_BITPAY;

-- Criar um usuário para a aplicação 
CREATE USER app_bitpay_user IDENTIFIED BY OutraSenhaSegura456;

-- 5. Dar ao usuário da aplicação a permissão de conectar e o ROLE
GRANT CONNECT TO app_bitpay_user;
GRANT ROLE_APP_BITPAY TO app_bitpay_user;