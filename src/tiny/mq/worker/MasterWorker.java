package tiny.mq.worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
            mainThreadPool = Executors.newScheduledThreadPool(3);
            serverSocket.setSoTimeout(300);
        }catch (Exception e){
            System.exit(0);
        }
    }

    public void handleRequest(){
        //while (true){
            mainThreadPool.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println("In socket waiting");
                    Socket socketTask = null;
                    try {
                        socketTask = serverSocket.accept();
                        dispatcher.dispatch(socketTask);
                    } catch (SocketTimeoutException e){
                        Thread.currentThread().interrupt();
                        System.out.println("Accept timeout");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 3, 10, TimeUnit.MILLISECONDS);

            mainThreadPool.scheduleAtFixedRate(dispatcher.tmqManager.new PersistenceTask(),2,10,TimeUnit.MILLISECONDS);
        //}
    }

    public int getPort(){
        return port;
    }

}
