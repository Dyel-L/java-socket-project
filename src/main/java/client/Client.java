package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        System.out.println("=== CLIENTE INICIADO ===");
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado ao servidor. Aguardando opcoes...");

            while (true) {
                String serverMsg = in.readLine();
                if (serverMsg == null) break;

                if (serverMsg.startsWith("MENU:")) {
                    System.out.println(serverMsg.substring(5));
                    System.out.print("> ");
                    String choice = scanner.nextLine();
                    out.println(choice);
                }
                else if (serverMsg.startsWith("PROMPT:")) {
                    System.out.println(serverMsg.substring(7));
                    System.out.print("> ");
                    String message = scanner.nextLine();
                    out.println(message);
                }
                else if (serverMsg.startsWith("RESULT:")) {
                    System.out.println("Resultado criptografado: " + serverMsg.substring(7));
                }
                else if (serverMsg.startsWith("OPTION:")) {
                    System.out.println(serverMsg.substring(7));
                    System.out.print("> ");
                    String option = scanner.nextLine();
                    out.println(option);
                }
                else if (serverMsg.startsWith("SAIR:")) {
                    System.out.println(serverMsg.substring(5));
                    break;
                }
                else if (serverMsg.startsWith("ALGORITHM:")) {
                    // Apenas para debug - pode ser removido
                    System.out.println("Algoritmo selecionado: " + serverMsg.substring(10));
                }
                else {
                    System.out.println("Mensagem do servidor: " + serverMsg);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        } finally {
            System.out.println("Conexão encerrada.");
        }
    }
}
