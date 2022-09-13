
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author MASTER
 */
public class ChatServer {

    private final int PORT = 8089;
    private ServerSocket serverSocket;
    private final List<ClientSocket> clients = new LinkedList();

    public void start() throws IOException {

        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor Inicializado na Porta! " + PORT);
        System.out.println("Aguardando Conexao ......");
        ClientConectionLoop();
    }

    private void ClientConectionLoop() throws IOException {

        while (true) {

            ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
            System.out.println("Cliente Conectado " + clientSocket.getRemoteSocketAddress());
            clients.add(clientSocket);
            InetAddress host = serverSocket.getInetAddress();
            String msg = "Voce esta Conectado no Servidor JavaSD";
            clientSocket.sendMsg(msg);

            new Thread(() -> clientMessageLoop(clientSocket)).start();

        }

    }

    public void clientMessageLoop(ClientSocket clientSocket) {

        String msg;

        while ((msg = clientSocket.getMessage()) != null) {
            if ("sair#$%".equalsIgnoreCase(msg)) {
                clientSocket.sendMsg("Desconectado!");
                break;
            }
            System.out.printf("Mensagem recebida do cliente %s: %s\n", clientSocket.getRemoteSocketAddress(), msg);
            //clientSocket.sendMsg("[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ]" + " Servidor: " + msg);
            Broadcasting(clientSocket, msg);
        }
        try {
            clientSocket.closeInOut();
            System.out.println("Socket fechado para o cliente " + clientSocket.getRemoteSocketAddress());

        } catch (IOException ex) {
            System.out.println("Problemas ao fechar o socket");
        }
        //return;

    }

    private void Broadcasting(ClientSocket sender, String msg) {

        Iterator<ClientSocket> iterator = clients.iterator();
        while (iterator.hasNext()) { //percorres a list clients
            ClientSocket clientSocket = iterator.next();
            if (!sender.equals(clientSocket)) { //parâmetro sendo verifica o remetente da msg, assim evita enviar o mensagem pra vc mesmo
                if (!clientSocket.sendMsg("[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ]" + " Usuário: " + msg)) {
                    iterator.remove();
                }

            }

        }

    }

    public static void main(String[] args) {

        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException ex) {
            System.out.println("Erro ao Iniciar o servidor :" + ex.getMessage());
        }
        System.out.println("Servidor finalizado!");
    }
}
