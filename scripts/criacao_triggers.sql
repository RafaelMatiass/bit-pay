select * from Clientes;
select * from Contas;
select * from Telefones;
select * from Enderecos;
select * from Usuarios;
select * from AplicacoesInvestimentos; 

select * from movimentacoes; 
select * from TiposMovimento;

insert into tipomovimento(6, 'RESGATE');
update AplicacoesInvestimentos
set dataaplicacao = TO_DATE('2025-08-06', 'YYYY-MM-DD')
where id = 7;