package tiny.mq.container;

import tiny.mq.message.Message;

public interface MessageQueue {
    void push(Message message);
    Message pop();
    int getLength();
}
