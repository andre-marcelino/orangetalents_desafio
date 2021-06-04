package com.orangetalents.desafio.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orangetalents.desafio.dto.UsuarioResponseDTO;
import com.orangetalents.desafio.dto.UsuarioSaveDTO;
import com.orangetalents.desafio.model.Usuario;
import com.orangetalents.desafio.service.UsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	ModelMapper mapper;
	
	@PostMapping
	public ResponseEntity<UsuarioSaveDTO> salvar(@Valid @RequestBody UsuarioSaveDTO dto) {
		Usuario entity = mapper.map(service.salvar(dto), Usuario.class);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
		return ResponseEntity.created(uri).body(mapper.map(entity, UsuarioSaveDTO.class));
	}
	
	@GetMapping
	public ResponseEntity<List<Usuario>> buscaTodos() {
		return ResponseEntity.ok(service.buscaTodos());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UsuarioResponseDTO> buscaPorId(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok(service.buscaVeiculos(id));
	}
}