package com.orangetalents.desafio.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangetalents.desafio.clients.FipeClient;
import com.orangetalents.desafio.clients.ResponseObj;
import com.orangetalents.desafio.dto.VeiculoResponseDTO;
import com.orangetalents.desafio.dto.VeiculoSaveDTO;
import com.orangetalents.desafio.exceptions.ServiceException;
import com.orangetalents.desafio.model.Usuario;
import com.orangetalents.desafio.model.Veiculo;
import com.orangetalents.desafio.repository.VeiculoRepository;

@Service
public class VeiculoService {

	@Autowired
	VeiculoRepository repository;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	FipeClient fipe;

	@Autowired
	ModelMapper mapper;

	@Transactional
	public Veiculo salvar(VeiculoSaveDTO dto) {
		usuarioService.buscaPorId(dto.getusuarioId());
		List<String> carro = validaVeiculo(dto);
		Veiculo entity = mapper.map(dto, Veiculo.class);
		entity.setValor(buscaValor(carro.get(0), carro.get(1), carro.get(2)));
		return repository.save(entity);
	}

	private List<String> validaMarca(VeiculoSaveDTO dto) {
		ResponseObj marca = fipe.listaMarcas().stream().
				filter(x -> x.getNome().toUpperCase().contains(dto.getMarca().toUpperCase())).
				findFirst().orElseThrow(() -> new ServiceException("Marca não existe"));
		
		return Arrays.asList(marca.getCodigo(), marca.getNome());
	}
	
	private List<String> validaModelo(VeiculoSaveDTO dto, String codigoMarca) {
		ResponseObj modelo = fipe.listaModelos(codigoMarca).getModelos().stream().
				filter(x -> x.getNome().toUpperCase().contains(dto.getModelo().toUpperCase())).
				findFirst().orElseThrow(() -> new ServiceException("Modelo não existe"));
	
		return Arrays.asList(modelo.getCodigo(), modelo.getNome());
	}
	
	private String validaAno(VeiculoSaveDTO dto, String codigoMarca, String codigoModelo) {
		return fipe.listaAnos(codigoMarca, codigoModelo).stream().
				filter(x -> x.getNome().contains(dto.getAno())).findFirst().
				orElseThrow(() -> new ServiceException("Ano não existe")).getCodigo();
		
	}

	private List<String> validaVeiculo(VeiculoSaveDTO dto) {
		List<String> marca = validaMarca(dto);
		List<String> modelo = validaModelo(dto, marca.get(0));
		String codigoAno = validaAno(dto, marca.get(0), modelo.get(0));
		dto.setMarca(marca.get(1));
		dto.setModelo(modelo.get(1));
		return Arrays.asList(marca.get(0), modelo.get(0), codigoAno);
	}
	
	private String buscaValor(String codigoMarca, String codigoModelo, String codigoAno) {
		return fipe.getCarro(codigoMarca, codigoModelo, codigoAno).getValor();
	}
	
	public List<VeiculoSaveDTO> buscaTodos() {
		return repository.findAll().stream().map(x -> mapper.map(x, VeiculoSaveDTO.class)).collect(Collectors.toList());
	}

	public List<VeiculoResponseDTO> buscaPorUsuario(Usuario usuario) {
		List<VeiculoResponseDTO> veiculos = repository.findByUsuario(usuario).stream().map(x -> mapper.map(x, VeiculoResponseDTO.class))
		.collect(Collectors.toList());
		veiculos.forEach(x -> setRodizio(x));
		return veiculos;
	}
	
	
	private void setRodizio(VeiculoResponseDTO dto) {
		char finalAno = dto.getAno().charAt(3);
		DayOfWeek dia = DayOfWeek.SUNDAY;
		
		switch (finalAno) {
		case '0':
		case '1':
			dia = DayOfWeek.MONDAY;
			break;
		case '2':
		case '3':
			dia = DayOfWeek.TUESDAY;
			break;
		case '4':
		case '5':
			dia = DayOfWeek.WEDNESDAY;
			break;
		case '6':
		case '7':
			dia = DayOfWeek.THURSDAY;
			break;
		case '8':
		case '9':
			dia = DayOfWeek.FRIDAY;
			break;
		}
		
		LocalDate agora = LocalDate.now();
		
		if(dia == agora.getDayOfWeek()) dto.setRodizioAtivo(true);
		
		dto.setDiaRodizio(dia.getDisplayName(TextStyle.FULL, new Locale("pt", "BR")));
	}
	
}
