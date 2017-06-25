package tiny.mq.message;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TinyMessageQueue implements MessageQueue {
    private ConcurrentLinkedDeque<Message> container;

    public TinyMessageQueue(){
        container = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void push(Message message) {
        container.push(message);
    }

    @Override
    public Message pop() {
        Message message = null;
        try{
            message  = container.pop();
        }catch (NoSuchElementException e){
            return message;
        }
        return message;
    }

    @Override
    public int getLength() {
        return container.size();
    }
}
