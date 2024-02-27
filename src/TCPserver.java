import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPserver {

    private static final int PORT = 6789;
    private static final int MAX_CLIENTS = 10;

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started. Listening on port " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket));
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    // Simulate processing delay
                    try {
                        Thread.sleep(1000); // 1 second sleep
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread was interrupted, Failed to complete operation");
                    }
                    // Process and respond
                    out.println(new StringBuilder(inputLine).reverse().toString());
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port or listening for a connection");
                System.out.println(e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    // Ignore IOException on close
                }
            }
        }
    }
}
