package tiny.mq.worker;

import tiny.mq.message.Message;
import tiny.mq.container.TinyMessageQueue;
import tiny.mq.utility.MessageAux;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TinyMessageQueueManager {
    private TinyMessageQueue[] tmq;

    private static Logger logger;

    class ParserMessageFromSocketAndInqueue implements Runnable{
        private Socket task;

        public ParserMessageFromSocketAndInqueue(Socket task){
            this.task = task;
        }

        @Override
        public void run() {
            synchronized (this) {
                try {
                    List<Message> messages = MessageAux.readMessagesFrom(task);

                    for (Message message : messages){
                        System.out.println(message);
                        findMinLengthQueue().push(message);
                    }
                } catch (IOException e) {
                    logger.warning("failure to push message into queue");
                }
            }
        }
    }

    class PersistenceTask implements Runnable{
        @Override
        public void run(){
            synchronized (this) {
                Message messageObject = findMaxLengthQueue().pop();
                if (messageObject != null) {
                    long epoch = System.currentTimeMillis() / 1000;
                    try(FileOutputStream fileOutputStream = new FileOutputStream("msgobj/msg" + epoch);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
                        objectOutputStream.writeObject(messageObject);
                        objectOutputStream.flush();
                        fileOutputStream.flush();
                        objectOutputStream.close();
                        fileOutputStream.close();
                        logger.info(Thread.currentThread().getName() + " : In persistence task with "+getTotalMessageCount());
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.warning("failure to persist message object");
                    }
                }
            }
        }
    }

    synchronized public int getTotalMessageCount(){
        int result = 0;
        for (TinyMessageQueue tmq : tmq) {
            result += tmq.getLength();
        }
        return result;
    }

    public TinyMessageQueueManager(int maxQueueCount){
        tmq = new TinyMessageQueue[maxQueueCount];
        for(int i = 0;i < maxQueueCount; i++){
            tmq[i] = new TinyMessageQueue();
        }
    }

    public TinyMessageQueue findMinLengthQueue(){
        int minLen = tmq[0].getLength();

        TinyMessageQueue tmqRef = tmq[0];

        for(int i = 1; i < tmq.length; i++){
            int curlen = tmq[i].getLength();
            if(curlen < minLen ){
                minLen = curlen;
                tmqRef = tmq[i];
            }
        }
        return tmqRef;
    }

    public TinyMessageQueue findMaxLengthQueue(){
        int maxLen = tmq[0].getLength();

        TinyMessageQueue tmqRef = tmq[0];

        for(int i = 1; i < tmq.length; i++){
            int curlen = tmq[i].getLength();
            if(curlen > maxLen ){
                maxLen = curlen;
                tmqRef = tmq[i];
            }
        }
        return tmqRef;
    }


    protected void switchLogger(){
        logger = Logger.getLogger("TMQ::LOGGER");
        logger.setLevel(Level.ALL);
    }
}
