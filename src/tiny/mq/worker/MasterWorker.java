package tiny.mq.worker;

import tiny.mq.utility.FolderAux;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class MasterWorker {
    private ServerSocket serverSocket;

    private DispatchMiddleware dispatcher;

    private ScheduledExecutorService mainThreadPool;

    private int port;

    public MasterWorker(int port) {
        try {
            this.port = port;
            serverSocket = new ServerSocket(port);
            dispatcher = new DispatchMiddleware();
            mainThreadPool = Executors.newScheduledThreadPool(10);
            serverSocket.setSoTimeout(300);
        }catch (Exception e){
            System.exit(0);
        }
    }

    public void configureWorker(){
        FolderAux.createFolder("msgobj");
    }

    public void handleRequest(){
        mainThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("In socket waiting");
                Socket socketTask = null;
                try {
                    socketTask = serverSocket.accept();
                    dispatcher.dispatch(socketTask);
                } catch (SocketTimeoutException e){
                    System.out.println("Accept timeout");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 3, 10, TimeUnit.MILLISECONDS);

        mainThreadPool.scheduleAtFixedRate(dispatcher.tmqManager.new PersistenceTask(), 2, 1, TimeUnit.SECONDS);
    }

    public int getPort(){
        return port;
    }

}
