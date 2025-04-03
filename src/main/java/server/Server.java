package server;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class Server {
    private final ServerSocket serverSocket;
    private final ThreadPoolManager threadPoolManager;
    private static final String AES_KEY = "1234567890123456";

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

                while (true) {
                    // Envia o menu de opções
                    out.println("MENU:Escolha o tipo de criptografia: (1-Criptografia Caesar) (2-Criptografia AES) (3-Sair)");

                    String choice = in.readLine();
                    if (choice == null || choice.equals("3")) {
                        out.println("SAIR:Saindo...");
                        break;
                    }

                    String algorithm = choice.equals("1") ? "CAESAR" : "AES";
                    out.println("ALGORITHM:" + algorithm);

                    while (true) {
                        out.println("PROMPT:Digite uma mensagem para ser criptografada (ou 'sair' para encerrar):");
                        String message = in.readLine();

                        if (message == null || message.equalsIgnoreCase("sair")) {
                            out.println("SAIR:Saindo...");
                            break;
                        }

                        String encrypted = choice.equals("1")
                                ? encryptCaesar(message, 3)
                                : encryptAES(message, AES_KEY);
                        out.println("RESULT:" + encrypted);

                        out.println("OPTION:Deseja retornar ao menu (1) ou continuar (2)?");
                        String option = in.readLine();

                        if (option == null || option.equals("1")) {
                            break; // Volta ao menu
                        }
                    }
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

    private static String encryptAES(String text, String key) throws IOException {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IOException("Erro na criptografia AES", e);
        }
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
