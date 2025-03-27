package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Digite uma mensagem para ser criptografada (ou 'sair' para encerrar):");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("sair")) {
                    break;
                }

                // Envia a mensagem para o servidor
                out.println(message);

                // Recebe e exibe a mensagem criptografada
                String response = in.readLine();
                System.out.println("Mensagem criptografada do servidor: " + response);
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
