package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final ThreadPoolManager threadPoolManager;

    // Construtor original que usa porta e poolSize
    public Server(int port, int poolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);  // Criação do ServerSocket com a porta
        this.threadPoolManager = new ThreadPoolManager(poolSize);
    }

    // Novo construtor que recebe um ServerSocket
    public Server(ServerSocket serverSocket, int poolSize) throws IOException {
        this.serverSocket = serverSocket;  // Usando o ServerSocket fornecido
        this.threadPoolManager = new ThreadPoolManager(poolSize);
    }

    public void start() {
        System.out.println("Servidor iniciado na porta " + serverSocket.getLocalPort());

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String receivedMessage;
                while ((receivedMessage = in.readLine()) != null) {
                    System.out.println("Recebido: " + receivedMessage);

                    // Criptografando a mensagem com deslocamento 3
                    String encryptedMessage = encryptCaesar(receivedMessage, 3);
                    System.out.println("Enviando criptografado: " + encryptedMessage);

                    out.println(encryptedMessage);
                }

                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro no servidor: " + e.getMessage());
            }
        }
    }

    private static String encryptCaesar(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isUpperCase(character) ? 'A' : 'a';
                result.append((char) ((character - base + shift) % 26 + base));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        try {
            int port = 12345;
            ServerSocket serverSocket = new ServerSocket(port);
            Server server = new Server(serverSocket, 10);
            server.start();
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}
