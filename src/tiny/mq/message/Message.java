package tiny.mq.message;

import java.io.Serializable;
import java.util.zip.CRC32;

public class Message implements Serializable {
    private int messageID;

    private int messageLength;

    private String messageContent;

    private long messageChecksumCode;


    public Message(int messageID,int messageLength,String messageContent){
        this.messageID = messageID;
        this.messageLength = messageLength;
        this.messageContent = messageContent;
        this.messageChecksumCode = getChecksumCode();
    }

    private long getChecksumCode(){
        String sb = String.valueOf(messageID) +
                messageLength +
                messageContent;
        CRC32 crc32 = new CRC32();
        crc32.update(sb.getBytes());
        return crc32.getValue();
    }

    public String toString(){
        return "[id:"+messageID+",length:"+messageLength+",content:"+messageContent+",checksumcode:"+messageChecksumCode+"]";
    }
}
