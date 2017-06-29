package tiny.mq.worker;

import tiny.mq.utility.FolderAux;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterWorker {
    public static Logger logger;

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
        logger = Logger.getLogger("TMQ::LOGGER");
        logger.setLevel(Level.ALL);
    }

    public void handleRequest(){
        mainThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                MasterWorker.logger.info("In socket waiting");
                Socket socketTask = null;
                try {
                    socketTask = serverSocket.accept();
                    dispatcher.dispatch(socketTask);
                } catch (SocketTimeoutException e){
                    MasterWorker.logger.info("Accept timeout");
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
