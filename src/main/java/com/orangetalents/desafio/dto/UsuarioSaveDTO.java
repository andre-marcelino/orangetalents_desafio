package com.orangetalents.desafio.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UsuarioSaveDTO {
	
	@Size(min = 2, message = "O nome precisa ter dois ou mais caracteres")
	private String nome;
	
	@Email(message = "Email inválido")
	private String email;
	
	@Pattern(regexp = "^[0-9]{11}$", message = "O CPF deve conter onze digitos, sem traços ou pontos")
	@CPF(message = "O CPF inserido não é válido")
	private String cpf;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@NotNull
	@Past(message = "Insira uma data de nascimento anterior")
	private LocalDate dataNascimento;
	
	public UsuarioSaveDTO() {}
	
	public UsuarioSaveDTO(String nome, String email, String cpf, LocalDate dataNascimento) {
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
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
}
