
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author MASTER
 */
public class ChatClient {

    /**
     * @param args the command line arguments
     */
    private static final String SERVER_ADDRESS = "ws://ssh.chauchuty.cf";
    private ClientSocket clientSocket;
    private final Scanner scanner;
    private PrintWriter out;

    public ChatClient() {

        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        clientSocket = new ClientSocket(
                new Socket(SERVER_ADDRESS, 8081));

        System.out.println("Cliente conectado ao Servidor!");
        messageLoop();
    }

    private void messageLoop() throws IOException {

        String msg;

        do {
            System.out.print("Digite sua mensagem ou Sair para finalizar a aplicacao  ");
            msg = this.scanner.nextLine();
            clientSocket.sendMsg(msg);

            new Thread(() -> clientMessageReturnLoop(clientSocket)).start();

        } while (!msg.equalsIgnoreCase("sair"));

    }

    public void clientMessageReturnLoop(ClientSocket clientSocket) {

        String msg;

        while ((msg = clientSocket.getMessage()) != null) {
            if ("sair".equalsIgnoreCase(msg)) {
                return;
            }
            System.out.printf("Mensagem recebida do cliente %s: %s\n", clientSocket.getRemoteSocketAddress(), msg);
            
        }

    }

    public static void main(String[] args) {
        // TODO code application logic here

        try {
            ChatClient cliente = new ChatClient();
            cliente.start();
        } catch (IOException ex) {
            System.out.println("Erro ao conectar com o Servidor! " + ex.getMessage());
        }
        System.out.println("Cliente Desconectou-se!");
    }

}
