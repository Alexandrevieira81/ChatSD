
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

            InetAddress host = serverSocket.getInetAddress();
            String msg = "Voce esta Conectado no Servidor JavaSD";

            new Thread(() -> clientMessageLoop(clientSocket)).start();
            clientSocket.sendMsg(msg);

        }

    }

    public void clientMessageLoop(ClientSocket clientSocket) {

        String msg;
        clientSocket.setLogin(clientSocket.getMessage());
        /*Movido para esse método,´pois no método ClientConectionLoop
          ele trava o cliente. Tanta a tribuição do nome quanto a adição
          na lista de clientes tem que ficar fora do while.
        */
        clients.add(clientSocket);

        while ((msg = clientSocket.getMessage()) != null) {
            if ("sair#$%".equalsIgnoreCase(msg)) {
                clientSocket.sendMsg("Desconectado!");
                break;
            }
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
            System.out.println(clientSocket.getLogin());

            if (!sender.equals(clientSocket)) {
                //parâmetro sendo verifica o remetente da msg, assim evita enviar o mensagem pra vc mesmo
                if (!clientSocket.sendMsg("[ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ]" + sender.getLogin() + " " + msg)) {
                    //caso servidor tente mandar a mensagem e o cliente não respoder ele remove o cliente da lista
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
