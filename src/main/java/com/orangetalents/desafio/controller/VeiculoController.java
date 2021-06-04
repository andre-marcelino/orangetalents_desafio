package com.orangetalents.desafio.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orangetalents.desafio.dto.VeiculoSaveDTO;
import com.orangetalents.desafio.model.Veiculo;
import com.orangetalents.desafio.service.VeiculoService;

@RestController
@RequestMapping(value = "/veiculos")
public class VeiculoController {
	
	@Autowired
	VeiculoService service;
	
	@Autowired
	ModelMapper mapper;
	
	@PostMapping
	public ResponseEntity<VeiculoSaveDTO> salvar(@Valid @RequestBody VeiculoSaveDTO dto) {
		Veiculo entity = service.salvar(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
		return ResponseEntity.created(uri).body(mapper.map(entity, VeiculoSaveDTO.class));
	}
	
	@GetMapping
	public ResponseEntity<List<VeiculoSaveDTO>> buscaTodos() {
		return ResponseEntity.ok(service.buscaTodos());
	}
}
