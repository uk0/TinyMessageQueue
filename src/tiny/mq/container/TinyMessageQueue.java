package tiny.mq.container;

import tiny.mq.message.Message;

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
        return container.pop();
    }

    @Override
    public int getLength() {
        return container.size();
    }
}
