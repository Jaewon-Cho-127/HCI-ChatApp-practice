import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 8888;
    private static List<Socket> clientSockets = new ArrayList<>();
    private static final String IMAGE_END = "IMAGE_END:";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("서버가 시작되었습니다.");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트가 접속했습니다.");

                clientSockets.add(clientSocket);

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                boolean isImage = false;
                StringBuilder imageBuilder = new StringBuilder();
                while ((message = reader.readLine()) != null) {
                    System.out.println("메시지 수신: " + message);

                    if(message.startsWith("IMAGE:")) {
                        isImage = true;
                        imageBuilder.append(message.substring(6)); // Strip "IMAGE:"
                    } else if (isImage && !message.equals(IMAGE_END)) {
                        imageBuilder.append(message);
                    } else if(message.equals(IMAGE_END)) {
                        System.out.println("이미지 데이터 수신");
                        sendToAllClients("IMAGE:" + imageBuilder.toString());
                        sendToAllClients(IMAGE_END);
                        isImage = false;
                        imageBuilder = new StringBuilder();
                    } else {
                        sendMessageToAllClients(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!clientSocket.isClosed()) {
                        reader.close();
                        writer.close();
                        clientSocket.close();
                        clientSockets.remove(clientSocket);
                        System.out.println("클라이언트가 연결을 종료했습니다.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendMessageToAllClients(String message) {
            for (Socket clientSocket : clientSockets) {
                try {
                    PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientWriter.println(message);
                    clientWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendToAllClients(String message) {
            for (Socket clientSocket : clientSockets) {
                try {
                    PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientWriter.println(message);
                    clientWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}