package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private final ExecutorService executor;

    public ThreadPoolManager(int poolSize) {
        // Cria o pool de threads
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public void execute(Runnable task) {
        // Executa a tarefa
        executor.execute(task);
    }

    public void shutdown() {
        // Encerra o pool de threads
        executor.shutdown();
    }
}
