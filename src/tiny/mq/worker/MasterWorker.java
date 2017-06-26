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
        while (true){
            dispatcher.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Socket socketTask = null;
                    try {
                        socketTask = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dispatcher.dispatch(socketTask);
                }
            });

            dispatcher.threadPool.execute(dispatcher.tmqManager.new PersistenceTask());
        }
    }

    public int getPort(){
        return port;
    }

}
