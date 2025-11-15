-- Este script deve ser executado pelo usuário 'bitpay_schema'

--============================================
-- Tabelas secundárias (Enum)
--============================================

-- Tabela: TiposUsuario
CREATE SEQUENCE seq_TiposUsuario 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE TiposUsuario (
    id NUMBER(10) PRIMARY KEY,
    tipo VARCHAR2(20 CHAR) NOT NULL
);

-- Tabela: StatusConta
CREATE SEQUENCE seq_StatusConta 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE StatusConta (
    id NUMBER(10) PRIMARY KEY,
    nomeStatus VARCHAR2(50 CHAR) NOT NULL
);

-- Tabela: TiposInvestimento
CREATE SEQUENCE seq_TiposInvestimento 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE TiposInvestimento (
    id NUMBER(10) PRIMARY KEY,
    nome VARCHAR2(150 CHAR) NOT NULL,
    rentabilidadeMes FLOAT NOT NULL,
    carenciaDias NUMBER(10) NOT NULL
);

-- Tabela: TiposMovimento
CREATE SEQUENCE seq_TiposMovimento
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE TiposMovimento (
    id NUMBER(10) PRIMARY KEY,
    tipoMovimento VARCHAR2(50 CHAR) NOT NULL
);

-- Tabela: statusEmprestimo
CREATE SEQUENCE seq_statusEmprestimo 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE StatusEmprestimo (
    id NUMBER(10) PRIMARY KEY,
    descricao VARCHAR2(20 CHAR) NOT NULL
);

-- Tabela: statusParcelasEmprestimo
CREATE SEQUENCE seq_statusParcelasEmprestimo 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE StatusParcelasEmprestimo (
    id NUMBER(10) PRIMARY KEY,
    descricao VARCHAR2(50 CHAR) NOT NULL
);

--============================================
-- Tabelas Principais (Entidades)
--============================================

-- Tabela: Usuarios
CREATE SEQUENCE seq_Usuarios 
    START WITH 1 
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
CREATE TABLE Usuarios (
    id NUMBER(10) PRIMARY KEY,
    CPF VARCHAR2(11 CHAR) NOT NULL UNIQUE,
    senha VARCHAR2(50 CHAR) NOT NULL UNIQUE,
    email VARCHAR2(150 CHAR) NOT NULL UNIQUE,
    idTipoUsuario NUMBER(10) NOT NULL
);

-- Tabela: Enderecos
CREATE SEQUENCE seq_Enderecos 
    START WITH 1 
    INCREMENT BY 1
    NOCACHE 
    NOCYCLE;
CREATE TABLE Enderecos (
    id NUMBER(10) PRIMARY KEY,
    logradouro VARCHAR2(255 CHAR) NOT NULL,
    numero NUMBER(10),
    complemento VARCHAR2(255 CHAR),
    bairro VARCHAR2(150 CHAR) NOT NULL,
    cidade VARCHAR2(150 CHAR) NOT NULL,
    estado VARCHAR2(2 CHAR) NOT NULL,
    cep VARCHAR2(9 CHAR) NOT NULL
);

-- Tabela: Telefones
CREATE SEQUENCE seq_Telefones 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE Telefones (
    id NUMBER(10) PRIMARY KEY,
    codPais NUMBER(10) NOT NULL,
    codArea NUMBER(10) NOT NULL,
    numero NUMBER(10) NOT NULL
);

-- Tabela: Gerentes
CREATE SEQUENCE seq_Gerentes 
    START WITH 1 
    INCREMENT BY 1
    NOCACHE 
    NOCYCLE;
CREATE TABLE Gerentes (
    id NUMBER(10) PRIMARY KEY,
    nome VARCHAR2(150 CHAR) NOT NULL,
    dataNascimento DATE NOT NULL,
    idUsuario NUMBER(10) NOT NULL
);

-- Tabela: Contas
CREATE SEQUENCE seq_Contas 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE Contas (
    id NUMBER(10) PRIMARY KEY,
    numeroConta VARCHAR2(10 CHAR) NOT NULL,
    saldo NUMBER(10,2),
    dataAbertura DATE NOT NULL,
    idStatusConta NUMBER(10) NOT NULL
);

-- Tabela: Clientes
CREATE SEQUENCE seq_Clientes 
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE Clientes (
    id NUMBER(10) DEFAULT seq_Clientes.NEXTVAL NOT NULL,
    nome VARCHAR2(150 CHAR) NOT NULL,
    dataNascimento DATE NOT NULL,
    dataCadastro DATE NOT NULL,
    idUsuario NUMBER(10) NOT NULL,
    idEndereco NUMBER(10) NOT NULL,
    idTelefone NUMBER(10) NOT NULL,
    idConta NUMBER(10) NOT NULL
);

-- Tabela: AplicacoesInvestimentos
CREATE SEQUENCE seq_AplicacoesInvestimentos
    START WITH 1 
    INCREMENT BY 1
    NOCACHE 
    NOCYCLE;
CREATE TABLE AplicacoesInvestimentos (
    id NUMBER(10) PRIMARY KEY,
    valorAplicado NUMBER(10,2) NOT NULL,
    dataAplicacao DATE NOT NULL,
    idConta NUMBER(10) NOT NULL,
    idTipoInvestimento NUMBER(10) NOT NULL
);

-- Tabela: Movimentacoes
CREATE SEQUENCE seq_Movimentacoes 
    START WITH 1 
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
CREATE TABLE Movimentacoes (
    id NUMBER(10) PRIMARY KEY,
    valor NUMBER(10,2) NOT NULL,
    dataMovimento DATE NOT NULL,
    idConta NUMBER(10) NOT NULL,
    idTipoMovimento NUMBER(10) NOT NULL
);

-- Tabela: Emprestimos
CREATE SEQUENCE seq_Emprestimos
    START WITH 1 
    INCREMENT BY 1 
    NOCACHE 
    NOCYCLE;
CREATE TABLE Emprestimos (
    id NUMBER(10) PRIMARY KEY,
    valorTotal NUMBER(10,2) NOT NULL,
    dataContratacao DATE NOT NULL,
    numerosParcelas NUMBER(10) NOT NULL,
    taxaJuros FLOAT NOT NULL,
    idConta NUMBER(10) NOT NULL,
    idStatusEmprestimo NUMBER(10) NOT NULL
);

-- Tabela: parcelasEmprestimos
CREATE SEQUENCE seq_parcelasEmprestimos 
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
CREATE TABLE ParcelasEmprestimos (
    id NUMBER(10) PRIMARY KEY,
    numeroParcela NUMBER(10) NOT NULL,
    valorParcela NUMBER(10,2) NOT NULL,
    dataVencimento DATE NOT NULL,
    valorPago NUMBER(10,2),
    dataPagamento DATE,
    idEmprestimo NUMBER(10) NOT NULL,
    idStatusParcela NUMBER(10) NOT NULL
);  

--============================================
-- Alters Tables (FK)
--============================================

-- Foreign Keys da Tabela: Usuarios
ALTER TABLE Usuarios 
ADD CONSTRAINT 
    fk_Usuarios_TipoUsuario FOREIGN KEY (idTipoUsuario) 
    REFERENCES TiposUsuario(id);

-- Foreign Keys da Tabela: Clientes
ALTER TABLE Clientes
ADD CONSTRAINT 
    fk_Clientes_Usuario FOREIGN KEY (idUsuario) 
    REFERENCES Usuarios(id);
    
ALTER TABLE Clientes 
ADD CONSTRAINT 
    fk_Clientes_Telefone FOREIGN KEY (idTelefone) 
    REFERENCES Telefones(id);
    
ALTER TABLE Clientes 
ADD CONSTRAINT 
    fk_Clientes_Endereco FOREIGN KEY (idEndereco) 
    REFERENCES Enderecos(id);
    
ALTER TABLE Clientes 
ADD CONSTRAINT 
    fk_Clientes_Conta FOREIGN KEY (idConta) 
    REFERENCES Contas(id);

-- Foreign Keys da Tabela: Gerentes
ALTER TABLE Gerentes 
ADD CONSTRAINT 
    fk_Gerentes_Usuario FOREIGN KEY (idUsuario) 
    REFERENCES Usuarios(id);

-- Foreign Keys da Tabela: Movimentacoes
ALTER TABLE Movimentacoes 
ADD CONSTRAINT 
    fk_Movimentacoes_Conta FOREIGN KEY (idConta)
    REFERENCES Contas(id);
    
ALTER TABLE Movimentacoes ADD CONSTRAINT
    fk_Movimentacoes_Tipo FOREIGN KEY (idTipoMovimento) 
    REFERENCES TiposMovimento(id);

-- Foreign Keys da Tabela: Contas
ALTER TABLE Contas 
ADD CONSTRAINT 
    fk_Contas_Status FOREIGN KEY (idStatusConta)
    REFERENCES StatusConta(id);

-- Foreign Keys da Tabela: AplicacoesInvestimentos
ALTER TABLE AplicacoesInvestimentos 
ADD CONSTRAINT 
    fk_Aplicacoes_Tipo FOREIGN KEY (idTipoInvestimento) 
    REFERENCES TiposInvestimento(id);
    
ALTER TABLE AplicacoesInvestimentos 
ADD CONSTRAINT 
    fk_Aplicacoes_Conta FOREIGN KEY (idConta) 
    REFERENCES Contas(id);

-- Foreign Keys da Tabela: Emprestimos
ALTER TABLE Emprestimos 
ADD CONSTRAINT 
    fk_Emprestimos_Conta FOREIGN KEY (idConta)
    REFERENCES Contas(id);
    
ALTER TABLE Emprestimos 
ADD CONSTRAINT 
    fk_Emprestimos_Status FOREIGN KEY (idStatusEmprestimo)
    REFERENCES StatusEmprestimo(id);

-- Foreign Keys da Tabela: parcelasEmprestimos
ALTER TABLE ParcelasEmprestimos ADD CONSTRAINT
    fk_Parcelas_Status FOREIGN KEY (idStatusParcela)
    REFERENCES StatusParcelasEmprestimo(id);
    
ALTER TABLE ParcelasEmprestimos ADD CONSTRAINT 
    fk_Parcelas_Emprestimo FOREIGN KEY (idEmprestimo)
    REFERENCES Emprestimos(id);

