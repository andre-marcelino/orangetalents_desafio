package com.orangetalents.desafio.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "fipe", url="https://parallelum.com.br/fipe/api/v1/carros")
public interface FipeClient {
	
	@GetMapping(value = "marcas")
	List<ResponseObj> listaMarcas();
	
	@GetMapping(value = "/marcas/{codigo}/modelos", params = "modelos" )
	ModeloAno listaModelos(@PathVariable(name = "codigo") String codigo);
	
	@GetMapping(value = "/marcas/{codigoMarca}/modelos/{codigoModelo}/anos")
	List<ResponseObj> listaAnos(@PathVariable(name = "codigoMarca") String codigoMarca, @PathVariable(value = "codigoModelo") String codigoModelo);
	
	@GetMapping(value = "/marcas/{codigoMarca}/modelos/{codigoModelo}/anos/{codigoAno}")
	ResponseCarro getCarro(@PathVariable(name = "codigoMarca") String codigoMarca, 
			@PathVariable(name = "codigoModelo") String codigoModelo,
			@PathVariable(name = "codigoAno") String codigoAno);
	
}