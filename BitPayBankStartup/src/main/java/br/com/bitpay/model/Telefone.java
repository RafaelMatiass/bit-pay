package br.com.bitpay.model;


public class Telefone {

    
    private int id; 
    private int codPais; 
    private int codArea;
    private long numero; 

    
    public Telefone(int codPais, int codArea, long numero) {
        this.codPais = codPais;
        this.codArea = codArea;
        this.numero = numero;
    }


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getCodPais() {
		return codPais;
	}


	public void setCodPais(int codPais) {
		this.codPais = codPais;
	}


	public int getCodArea() {
		return codArea;
	}


	public void setCodArea(int codArea) {
		this.codArea = codArea;
	}


	public long getNumero() {
		return numero;
	}


	public void setNumero(long numero) {
		this.numero = numero;
	}

    
}