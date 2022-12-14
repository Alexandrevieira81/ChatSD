
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientSocket {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ClientSocket(Socket socket) throws IOException {

        this.socket = socket;
        this.login = "";
        //System.out.println(" Cliente :"+socket.getRemoteSocketAddress()+ " : conectou");
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String getMessage() {

        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
    
    public boolean sendMsg(String msg){
        
        out.println(msg);
        return !out.checkError();
    }
    public void closeInOut() throws IOException{
        
        in.close();
        out.close();
        socket.close();
    }
    
    public SocketAddress getRemoteSocketAddress(){
    
        return socket.getRemoteSocketAddress();
    
    }
    

}
