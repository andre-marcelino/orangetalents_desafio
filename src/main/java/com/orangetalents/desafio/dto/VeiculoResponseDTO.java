package com.orangetalents.desafio.dto;

public class VeiculoResponseDTO {
	
	private String marca;
	private String modelo;
	private String ano;
	private String valor;
	private String diaRodizio;
	private Boolean rodizioAtivo = false;

	public VeiculoResponseDTO() {
	}

	public VeiculoResponseDTO(String marca, String modelo, String ano, String valor, String diaRodizio,
			Boolean rodizioAtivo) {
		this.marca = marca;
		this.modelo = modelo;
		this.ano = ano;
		this.valor = valor;
		this.diaRodizio = diaRodizio;
		this.rodizioAtivo = rodizioAtivo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDiaRodizio() {
		return diaRodizio;
	}

	public void setDiaRodizio(String diaRodizio) {
		this.diaRodizio = diaRodizio;
	}

	public Boolean getRodizioAtivo() {
		return rodizioAtivo;
	}

	public void setRodizioAtivo(Boolean rodizioAtivo) {
		this.rodizioAtivo = rodizioAtivo;
	}
}
