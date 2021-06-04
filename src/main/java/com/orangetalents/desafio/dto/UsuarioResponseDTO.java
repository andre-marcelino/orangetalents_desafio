package com.orangetalents.desafio.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UsuarioResponseDTO {
	
	private String nome;
	private String email;
	private String cpf;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;
	
	private List<VeiculoResponseDTO> veiculos = new ArrayList<>();
	
	public UsuarioResponseDTO() {}

	public UsuarioResponseDTO(String nome, String email, String cpf, LocalDate dataNascimento,
			List<VeiculoResponseDTO> veiculos) {
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.veiculos.addAll(veiculos);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public List<VeiculoResponseDTO> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<VeiculoResponseDTO> veiculos) {
		this.veiculos.addAll(veiculos);
	}
}
