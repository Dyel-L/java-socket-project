# Java Socket Project

Este projeto implementa uma comunicação entre processos utilizando sockets em Java. O servidor é capaz de gerenciar múltiplas conexões simultaneamente através de um ThreadPool, permitindo que vários clientes se conectem e se comuniquem de forma eficiente.

## Estrutura do Projeto

```
java-socket-project
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── server
│   │   │   │   ├── Server.java
│   │   │   │   ├── ClientHandler.java
│   │   │   │   └── ThreadPoolManager.java
│   │   │   └── client
│   │   │       └── Client.java
│   │   └── resources
│   └── test
│       └── java
│           └── server
│               └── ServerTest.java
├── build.gradle
├── settings.gradle
└── README.md
```

## Instruções de Instalação

1. Certifique-se de ter o Java Development Kit (JDK) instalado em sua máquina.
2. Clone este repositório em sua máquina local.
3. Navegue até o diretório do projeto.

## Compilação e Execução

Para compilar e executar o projeto, utilize o Gradle. Execute os seguintes comandos no terminal:

```bash
./gradlew build
./gradlew run
```

## Uso

1. Inicie o servidor executando a classe `Server`.
2. Conecte-se ao servidor utilizando a classe `Client`.
3. Envie mensagens e receba respostas.

## Implementação

- **Server.java**: Classe responsável por iniciar o servidor e gerenciar conexões.
- **ClientHandler.java**: Classe que lida com a comunicação de cada cliente.
- **ThreadPoolManager.java**: Gerencia um pool de threads para atender múltiplas conexões.
- **Client.java**: Classe que representa o cliente e permite a interação com o servidor.
- **ServerTest.java**: Contém testes unitários para garantir o funcionamento correto do servidor.

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

## Licença

Este projeto está licenciado sob a MIT License. Veja o arquivo LICENSE para mais detalhes.