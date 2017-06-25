package tiny.mq.worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterWorker {
    private ServerSocket serverSocket;

    private DispatchMiddleware dispatcher;

    private int port;

    public MasterWorker(int port) {
        try {
            this.port = port;
            serverSocket = new ServerSocket(port);
            dispatcher = new DispatchMiddleware();
        }catch (Exception e){
            System.exit(0);
        }
    }

    public void handleRequest(){
        try {
            while (true){
                Socket socketTask = serverSocket.accept();
                dispatcher.dispatch(socketTask);
            }
        } catch (IOException e) {}
    }

    public int getPort(){
        return port;
    }

}
