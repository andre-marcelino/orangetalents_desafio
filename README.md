# <font color=#ec7000> DESAFIO ORANGE TALENTS </font> :orange_heart:
***

## <font color=#ec7000> Introdução </font>
Neste desafio foi proposto construir uma API REST *(Representational State Transfer)*, na qual consiste em disponibilizar um cadastro de usuários, um cadastro de veículos e a listagem de veículos de um determinado usuário.

A implementação desse projeto foi realizada utilizando a linguagem **Java** na versão 11. O modelo de desenvolvimento adotado foi o **Waterfall**, essa adoção se justifica pelo fato do projeto ser de tamanho relativamente pequeno. Já o padrão arquitetural foi o **MVC**, que permitiu um baixo acoplamento entre as camadas do sistema. No entanto a camada *View* não foi desenvolvida.

## <font color=#ec7000>Principais Tecnologias </font>
### Spring Framework
Em poucas palavras o Spring é um framework Java que implementa o padrão de projeto **Inversão de Controle** *(IoC Inversion of Control)* através de **Injeção de Dependências** *(DI Dependency Injection)*. Isto é, o Spring abstrai a criação de objetos, deixando para o programador apenas a utilização desses objetos.

### Spring Boot
O Spring Boot, basicamente, gerencia toda a configuração de um projeto que utiliza o Spring Framework. Também disponibiliza automaticamente um servidor web, no caso o Apache Tomcat. O que faz aumentar a produtividade do desenvolvedor, já que quase toda a configuração se torna transparente e por consequência diminui a possibilidade de erros por parte do desenvolvedor.

### Spring Web MVC
Inicialmente o Spring Framework não foi idealizado para trabalhar com aplicações web. De modo a preencher essa lacuna, surgiu o Spring Web MVC. Com ele é possível construir uma aplicação direcionada a web com o padrão MVC.

### Spring Data JPA / Hibernate
Quando precisamos trabalhar com banco de dados, é a essa tecnologia que recorremos. O Spring Data JPA é uma biblioteca que utiliza o **Hibernate** como implementação da JPA *(Jakarta Persistence API)*. A JPA é uma especificação de como deve ser o tratamento de utilização com banco de dados, e o Hibernate é a implementação dessa especificação. Utilizar o Spring Data traz mais produtividade ao abstrair ações como a conexão e desconexão com o banco de dados. O Hibernate também implementa a especificação Jakarta Bean Validation, o que permite validar dados antes de realizar a persistência.

### Maven
Ao trabalhar com desenvolvimento, muitas vezes nos deparamos com diversos problemas que já foram solucionados por outras pessoas. Tais soluções são geralmente disponibilizadas através de bibliotecas e APIs, e quando utilizamos essas soluções em nosso código as chamamos de dependências. O **Maven** é um administrador de dependências e também um gerenciador de projeto. Assim, ao empregar essa tecnologia não precisamos lidar manualmente com as dependências de nosso código nem com o processo de build da aplicação.

## <font color=#ec7000> Implementação </font>
### Camada Model
Aqui nessa camada são construídas as classes entidades de nosso projeto. Ou seja, é a representação das tabelas do banco de dados. 

As classes entidades serão implementadas de acordo com o modelo conceitual abaixo.

![Diagrama de Classes](https://raw.githubusercontent.com/andre-marcelino/orangetalents_desafio/master/orangetalents%20Class%20diagram.png)

#### Classe Usuario
Todas as anotações dessa classe pertencem a JPA. A anotação `@Entity` determina o relacionamento entre a classe java e a tabela no banco de dados. Como boa prática em administração de banco de dados, cada registro da tabela deve ter um identificador único, fazemos isso utilizando as anotações `@Id` e `GeneratedValue`. `@Column` é a anotação que determina se o campo deve ser nulo ou não e se deve ser único ou não. Como podemos ver no modelo acima, a entidade Usuario é caracterizada por um relacionamento de composição com a entidade Veiculo, essa característica está atrelada à anotação `@OneToMany` na propriedade `veiculo` da classe `Usuario`, que significa um relacionamento de *um para muitos*.
``` java
// Imports omitidos
@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = false)
	private String nome;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, unique = true)
	private String cpf;
	
	@Column(nullable = false, unique = false)
	private LocalDate dataNascimento;
	
	@OneToMany(mappedBy = "usuario")
	private List<Veiculo> veiculo;

    // Construtores, getters e setters omitidos
}
```

#### Classe Veiculo
A estrtutura dessa classe é semelhante a classe anterior. A única diferença que temos aqui é a anotação `@ManyToOne`. Na propriedade `mappedBy = "usuario"` da anotação `OneToMany`, aplicada na classe `Usuario`, apontamos que o atributo `usuario` na classe `Veiculo` seria a chave estrangeira na tabela veiculo. Assim, a anotação `@ManyToOne`, que indica uma relacionamento de *muitos para um*, complementa essa ideia.
``` java
// Imports omitidos
@Entity
public class Veiculo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = false)
	private String marca;
	
	@Column(nullable = false, unique = false)
	private String modelo;
	
	@Column(nullable = false, unique = false)
	private String ano;
	
	@Column(nullable = false, unique = false)
	private String valor;
	
	@ManyToOne()
	private Usuario usuario;

    // Construtores, getters e setters omitidos
}
```

Abaixo estão as representações das tabelas do banco de dados relacionadas com as classes acima.
<font size="2pt">
##### Tabela usuario
ID|Nome|E-mail|CPF|Data de Nascimento|
--|----|------|---|------------------|
1|José|jose@gmail.com|79147397071|1991-01-01|

##### Tabela veiculo
ID|Marca|Modelo|Ano|Valor|ID_USUARIO|
--|-----|------|---|-----|------------|
1|VW - VolksWagen|Fusca|1985|R$ 10.370,00|1|
2|Renault|SANDERO Auth. Plus Hi-Power 1.0 16V 5p|2016|R$ 32.452,0|1| 
</font>

### Classes DTO
**DTO** *(Data Transfer Object)* é um padrão de projeto que tem o objetivo de transferir dados entre camadas de uma aplicação. Assim é garantida a consistência e o desacoplamento.

#### A classe UsuarioSaveDTO
Essa classe é responsável por transportar dados da camada `Controller` para a camada `Service`. Ela carrega os dados inseridos pelo usuário da API, que chegam da requisição, até o método de persistência presente na camada `Service`, ou seja, está relacionada com o endpoint de cadastro. A anotação `@JsonFormat` permite que a data de nascimento seja inserida no formato *dia/mês/ano*, que é o mais comum no Brasil. Todas as outras anotações são referentes a validação, e são autoexplicativas. 
``` java
// Imports omitidos
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

    // Construtores, getters e setters omitidos
}
```

#### Classe UsuarioResponseDTO
Diferente da classe `UsuarioSaveDTO`, essa é destinada a apresentação dos dados, portanto ela transita entre as camadas `Model` e `Service`. Ou seja, ela carrega os dados que estão persistidos no banco e os transfere para serem exibidos no endpoint de listagem do usuário.
``` java
// Imports omitidos
public class UsuarioResponseDTO {
	
	private String nome;
	private String email;
	private String cpf;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;
	
	private List<VeiculoResponseDTO> veiculos = new ArrayList<>();
    
    // Construtores, getters e setters omitidos
}
```

#### Classe VeiculoSaveDTO
Aqui o comportamento é semelhante ao da `UsuarioSaveDTO`. Uma observação é para o atributo `usuarioID`, que representa a chave estrangeira na tabela veiculo.
``` java
// Imports omitidos
public class VeiculoSaveDTO {
	
	@NotEmpty(message = "Marca: não pode ter o campo vazio")
	private String marca;
	
	@NotEmpty(message = "Marca: não pode ter o campo vazio")
	private String modelo;
	
	@Pattern(regexp = "^[0-9]{4}$", message = "Ano incorreto")
	private String ano;
	
	@NotNull
	private Long usuarioId;

    // Construtores, getters e setters omitidos
}
```

#### Classe VeiculoResponseDTO
Da mesma forma que a classe `UsuarioResponseDTO`, essa também é utilizada para levar dados ao endpoint de listagem do usuário. A novidade aqui são os atribuitos `diaRodizio` e `rodizioAtivo`, que cumprem os requisitos da regra de negócio estabelecida no desafio.
``` java
public class VeiculoResponseDTO {
	
	private String marca;
	private String modelo;
	private String ano;
	private String valor;
	private String diaRodizio;
	private Boolean rodizioAtivo = false;
    
    // Construtores, getters e setters omitidos
}
```

### Camada Service
A camada service abriga toda a lógica e os algoritmos para cumprir as regras de negócio. Ela conversa com as camadas `Model`, `Repository` e `Controller`.

#### Classe UsuarioService
A anotação `@Service` pertence ao Spring e é utilizada para caracterizar a classe como um componente da camada `Service`. A injeção de dependências está relacionada com a anotação `@Autowired` e também pertence ao Spring. `@Transactional` está relacionada com o gerenciamento da conexão com o banco de dados.

O método `salvar()` atua junto ao endpoint de cadastro do usuario. Ele coleta os dados recebidos na requisição e persiste no banco, se o método `validaUsuario()` não lançar nenhuma exceção. Ao final retorna o objeto persistido.

O endpoint da listagem de veículos de um usuário utiliza o método `buscaVeiculos()`.

``` java
// Imports omitidos
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
```

#### Classe VeiculoService
Nessa classe o método `salvar()` utiliza o método `validaVeiculo()` que por sua vez chama os métodos `validaMarca()`, `validaModelo()` e `validaAno()`, esses três métodos tem um comportamento idêntico entre si, variando apenas o atributo ao qual se referem. Vamos explicar o funcionamento deles tomando como base o método `validaMarca()`, caso o usuário da API não ensira o valor do campo idêntico ao que é provido pela API externa, que nossa aplicação utiliza, o método então tenta buscar a primeira ocorrência que contém o valor inserido, caso não encontre nenhuma ocorrência o método é encerrado com uma exceção. Por exemplo, suponha que o usuário insira o valor *"volks"* no campo marca, na API provida essa marca não existe, então o método procura pela primeira ocorrência que contenha *"volks"*, que no caso é *"VW Volkswagen"*, então esse valor é retornado pelo metódo e inserido no banco. Essa abordagem foi utilizada levando em consideração prover uma melhor usabilidade da nossa API. Durante a execução do método salvar, a API externa é utilizada também para buscar o valor do veiculo e posteriormente o mesmo ser persistido no banco.

Ao invocar o método `buscaPorUsuario()`, para exibir a listagem no endpoint dos dados do usuário, o método `setRodizio()` é chamado e então a regra de negócio referente ao veículo é executada. Note, portanto, que os atributos `diaRodizio` e `rodizioAtivo` da classe `VeiculoResponseDTO` são obtidos nesse momento, ou seja, não vieram do banco de dados. 
``` java
// Imports omitidos
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
```

### Camada Controller
Poderíamos dizer que essa camada é o porto da nossa API, pois é aqui que chegam e saem os dados. A camada `Controller` atua entre a camada `Service` e a camada `View`. Como não implementamos a camada `View`, a interação com o usuário é feita por aqui.

#### Classe UsuarioController
Para definir a classe como um componente da camada `Controller`, é utilizada a anotação `@RestController` do Spring. A anotação `@RequestMapping` define a URL pela qual a classe será invocada, nesse caso a URL refere-se ao endpoint de cadastro de usuário. Ao anotar o método `salvar()` com `@PostMapping`, estamos explicitando que quando o usuário fizer uma requisição do tipo __*POST*__, esse método será acionado.

O método `buscaPorId()` refere-se ao endpoint da listagem de dados de um usuário, no qual a lista de veículos que o mesmo possui está inserida. Esse método está anotado com `GetMapping` e está definindo uma URL, dessa forma quando o usuário fizer uma requisição do tipo __*GET*__ através da URL */usuarios/{id_do_usuario}*, será retornado um arquivo __*JSON*__ contendo os dados do objeto referente à classe `UsuarioResponseDTO`
``` java 
// Imports omitidos
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
```

#### Classe VeiculoController
Essa classe é semelhante a classe `UsuarioController`. O método `salvar()` refere-se ao endpoint de cadastro de veículo para um determinado usuário.
``` java
// Imports omitidos
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
```

### O Resto da Implementação
Até aqui foram apresentadas as classes mais relevantes da aplicação. O restante do código pode ser acessado no repositório do GitHub através do link: https://github.com/andre-marcelino/orangetalents_desafio.git

## <font color=#ec7000> Consumindo a API </font>
### Cadastrando um Usuário
![](https://raw.githubusercontent.com/andre-marcelino/orangetalents_desafio/master/cadastro_usuario.PNG)

### Inserindo Veículos
![](https://raw.githubusercontent.com/andre-marcelino/orangetalents_desafio/master/cadastro_veiculo.PNG)

![](https://raw.githubusercontent.com/andre-marcelino/orangetalents_desafio/master/cadastro_outro_veiculo.PNG)

### Listando os Veículos de um Usuário
![](https://raw.githubusercontent.com/andre-marcelino/orangetalents_desafio/master/exibindo_usuario.PNG)
