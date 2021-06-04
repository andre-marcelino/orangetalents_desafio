package com.orangetalents.desafio.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class VeiculoSaveDTO {
	
	@NotEmpty(message = "Marca: não pode ter o campo vazio")
	private String marca;
	
	@NotEmpty(message = "Marca: não pode ter o campo vazio")
	private String modelo;
	
	@Pattern(regexp = "^[0-9]{4}$", message = "Ano incorreto")
	private String ano;
	
	@NotNull
	private Long usuarioId;
	
	public VeiculoSaveDTO() {}
	
	public VeiculoSaveDTO(String marca, String modelo, String ano, Long usuarioId) {
		this.marca = marca;
		this.modelo = modelo;
		this.ano = ano;
		this.usuarioId = usuarioId;
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
	public Long getusuarioId() {
		return usuarioId;
	}
	public void setusuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
}
