package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTest {
    private ServerSocket serverSocket;
    private Thread serverThread;

    @BeforeEach
    public void setUp() throws IOException {
        serverSocket = new ServerSocket(12345);
        serverThread = new Thread(() -> {
            try {
                // Usando o novo construtor que recebe ServerSocket
                Server server = new Server(serverSocket, 10);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        serverThread.interrupt();  // Interrompe a thread do servidor
    }

    @Test
    public void testServerAcceptsConnection() throws IOException, InterruptedException {
        Thread.sleep(500);

        try (Socket socket = new Socket("localhost", 12345)) {
            assertTrue(socket.isConnected());
        }
    }

    @Test
    public void testServerHandlesMultipleConnections() throws IOException {
        int numberOfClients = 5;
        Thread[] clientThreads = new Thread[numberOfClients];

        for (int i = 0; i < numberOfClients; i++) {
            clientThreads[i] = new Thread(() -> {
                try (Socket socket = new Socket("localhost", 12345)) {
                    assertTrue(socket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientThreads[i].start();
        }

        for (Thread clientThread : clientThreads) {
            try {
                clientThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
