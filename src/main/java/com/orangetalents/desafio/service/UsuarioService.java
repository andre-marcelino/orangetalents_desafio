package com.orangetalents.desafio.service;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangetalents.desafio.dto.UsuarioResponseDTO;
import com.orangetalents.desafio.dto.UsuarioSaveDTO;
import com.orangetalents.desafio.exceptions.ServiceException;
import com.orangetalents.desafio.model.Usuario;
import com.orangetalents.desafio.repository.UsuarioRepository;

@Service
public class UsuarioService {
		
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	VeiculoService serviceVeiculos;
	
	@Autowired
	ModelMapper mapper;
	
	@Transactional
	public UsuarioSaveDTO salvar(UsuarioSaveDTO dto) {
		validaUsuario(dto);
		Usuario entity = mapper.map(dto, Usuario.class);
		return mapper.map(repository.save(entity), UsuarioSaveDTO.class);
	}
	
	private void validaUsuario(UsuarioSaveDTO dto) {
		Usuario usuario = repository.findByEmail(dto.getEmail());
		if(usuario != null) throw new ServiceException("Já existe usuário com o email inserido");
		usuario = repository.findByCpf(dto.getCpf());
		if(usuario != null) throw new ServiceException("Já existe usuário com o cpf inserido");
	}
	
	public List<Usuario> buscaTodos() {
		List<Usuario> result = repository.findAll();
		return result;
	}
	

	public Usuario buscaPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new ServiceException("Usuário não existe"));
	}
	
	public UsuarioResponseDTO buscaVeiculos(Long id) {
		UsuarioResponseDTO usuario = mapper.map(buscaPorId(id), UsuarioResponseDTO.class);
		usuario.setVeiculos(serviceVeiculos.buscaPorUsuario(buscaPorId(id)));
		return usuario;
	}
}
