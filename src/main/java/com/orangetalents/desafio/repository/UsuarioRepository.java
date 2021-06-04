package com.orangetalents.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orangetalents.desafio.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);

	Usuario findByCpf(String cpf);
	
}
