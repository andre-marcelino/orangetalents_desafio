package com.orangetalents.desafio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orangetalents.desafio.model.Usuario;
import com.orangetalents.desafio.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

	List<Veiculo> findByUsuario(Usuario usuario);
	
}
