package tiny.mq.message;

public interface MessageQueue {
    void push(Message message);
    Message pop();
    int getLength();
}
