package tiny.mq.worker;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatchMiddleware {
    protected ExecutorService threadPool;
    protected TinyMessageQueueManager tmqManager;


    public DispatchMiddleware(){
        tmqManager = new TinyMessageQueueManager(4);
        tmqManager.switchLogger();
        threadPool = Executors.newCachedThreadPool();
    }

    public void dispatch(Socket task){
        threadPool.execute(tmqManager.new ParserMessageFromSocketAndInqueue(task));
        threadPool.execute(tmqManager.new PersistenceTask());
    }


}
