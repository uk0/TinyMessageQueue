package test.tiny.mq.worker;

import tiny.mq.message.Message;
import tiny.mq.utility.MessageAux;

import java.io.IOException;

public class MessageAuxTest {
    static class testForDeserializable{
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            Message msg = MessageAux.readMessagesFrom("msg1498374247");
            System.out.println(msg);
        }
    }
}