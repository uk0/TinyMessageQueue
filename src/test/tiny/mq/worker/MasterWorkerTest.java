package test.tiny.mq.worker;

import tiny.mq.worker.MasterWorker;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

class RequestSimulator {
    private Socket socket;

    private OutputStream out;

    private String ip;

    private int port;

    public RequestSimulator(String IP,int PORT){
        try {
            ip = IP;
            port = port;
            socket = new Socket(IP,PORT);
        } catch (IOException e) {
            System.out.println("[I] failure to start request simulator");
        }
    }

    public void sendMessage(String str){
        try{
            out = socket.getOutputStream();
            out.write(str.getBytes());
        } catch (IOException e) {
            System.out.println("[I] failure to send message");
        }
    }

    public void close(){
        try {
            out.close();
            // Closing the returned OutputStream will close the associated
            // socket so that we do not have to close socket again
            // socket.close();
        } catch (IOException e) {
            System.out.println("[I] failure to close RequestSimulator socket");
        }
    }
}

public class MasterWorkerTest {
    static class testHandleRequest {
        public static void main(String[] args){
            MasterWorker master = new MasterWorker(5120);
            master.handleRequest();
        }

    }

    /*
     * testcase message are as follows
     * @11110001h
     * @11120005hello
     * @11130006你好
     * @00000010helloworld
     * @000100001x
     * @00020026qwertyuiopasdfghjklzxcvbnm
     * @00030012中文测试
     * @-1230001x
     */

    static class testHandleRequestWithUsersInput{
        public static void main(String[] args) throws Exception{
            RequestSimulator rs = new RequestSimulator("localhost",5120);
            Scanner scan = new Scanner(System.in);
            int i = 0;
            while (i < 3){
                System.out.println("Type message[" + i + "] you want to send:");
                String str = scan.nextLine();
                rs.sendMessage(str);
                i++;
            }
            rs.close();
        }
    }

    static class testHandleRequestWithoutUserInput{
        public static void main(String[] args){
            RequestSimulator rs = new RequestSimulator("localhost",5120);
            Scanner scan = new Scanner(System.in);
            int i = 0;
            while (i < 100){
                rs.sendMessage("@00020026qwertyuiopasdfghjklzxcvbnm");
                i++;
            }
            rs.close();
        }
    }
} 
