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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private static final String AES_KEY = "1234567890123456";
    private static final String XOR_KEY = "secret";

    public Server(int port, int poolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public Server(ServerSocket serverSocket, int poolSize) throws IOException {
        this.serverSocket = serverSocket;
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {
        System.out.println("Servidor iniciado na porta " + serverSocket.getLocalPort());

        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());

                threadPool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            while (true) {
                out.println("MENU:Escolha o tipo de criptografia: " +
                        "(1-Caesar) (2-AES) (3-XOR) (4-Base64) (5-Sair)");

                String choice = in.readLine();
                if (choice == null || choice.equals("5")) {
                    out.println("SAIR:Saindo...");
                    break;
                }

                String algorithm = getAlgorithmName(choice);
                out.println("ALGORITHM:" + algorithm);

                while (true) {
                    out.println("PROMPT:Digite uma mensagem para ser criptografada (ou 'sair' para encerrar):");
                    String message = in.readLine();

                    if (message == null || message.equalsIgnoreCase("sair")) {
                        out.println("SAIR:Saindo...");
                        break;
                    }

                    String encrypted;
                    try {
                        encrypted = switch (choice) {
                            case "1" -> encryptCaesar(message, 3);
                            case "2" -> encryptAES(message, AES_KEY);
                            case "3" -> encryptXOR(message, XOR_KEY);
                            case "4" -> encryptBase64(message);
                            default -> "Opção inválida";
                        };
                        out.println("RESULT:" + encrypted);
                    } catch (Exception e) {
                        out.println("ERROR:Erro durante a criptografia: " + e.getMessage());
                    }

                    out.println("OPTION:Deseja retornar ao menu (1) ou continuar (2)?");
                    String option = in.readLine();

                    if (option == null || !option.equals("2")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro no tratamento do cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar socket do cliente: " + e.getMessage());
            }
        }
    }

    private String getAlgorithmName(String choice) {
        return switch (choice) {
            case "1" -> "CAESAR";
            case "2" -> "AES";
            case "3" -> "XOR";
            case "4" -> "BASE64";
            default -> "UNKNOWN";
        };
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

    private static String encryptAES(String text, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static String encryptXOR(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char k = key.charAt(i % key.length());
            result.append((char) (c ^ k));
        }
        return Base64.getEncoder().encodeToString(result.toString().getBytes());
    }

    private static String encryptBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public void shutdown() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
            }
        } catch (IOException e) {
            System.err.println("Erro ao encerrar servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = null;
        try {
            int port = 12345;
            int poolSize = 10;
            server = new Server(port, poolSize);
            server.start();
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
        } finally {
            if (server != null) {
                server.shutdown();
            }
        }
    }
}