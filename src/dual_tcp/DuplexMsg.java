package dual_tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by gnidoc327 on 2017. 3. 29..
 */
public class DuplexMsg {
    private static int myport;
    private static ArrayList<EchoThread> threads;
    private static ServerSocket serverSocket;

    public static void main(String args[]) throws IOException {
        if(args.length != 1){
            printHelpForArgs();
            return;
        }

        threads = new ArrayList<>();

        myport = Integer.parseInt(args[0]);
        serverSocket = new ServerSocket(myport);
        System.out.println("server start port number="+myport);

        // 채팅 입력
        new Thread(){
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);

                while(true){
                    //입력시 커맨드 분석
                    String str = scanner.nextLine();
                    String[] strings = str.split(" ");
                    if(strings[0].equals("connect")){   //help참고
                        try {
                            connect(new Socket(strings[1], Integer.parseInt(strings[2])));
                        } catch (IOException e) {
                            //e.printStackTrace();
                        } catch (ArrayIndexOutOfBoundsException e){
                            printHelpForCommand();
                        }
                    //일반적인 입력은 send
                    } else {
                        for(EchoThread echoThread : threads){
                            echoThread.sendMsg(str);
                        }
                    }
                }
            }
        }.start();

        //접속 대기
        while(true){
            try {
                System.out.println("wait for connection");
                Socket socket = serverSocket.accept();

                connect(socket);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    private static class EchoThread extends Thread{
        private Socket socket;

        EchoThread(Socket socket) {
            this.socket = socket;
        }

        //수신
        @Override
        public void run() {
            while(true){
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    System.out.println(dataInputStream.readUTF());
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }

        //송신
        void sendMsg(String sendString){
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("send to "+socket.getInetAddress()+":"+socket.getPort()+" : "+sendString);
                dataOutputStream.writeUTF(sendString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void connect(Socket socket){
        System.out.println(socket.getInetAddress()+":"+socket.getPort()+" is connected");
        EchoThread echoThread = new EchoThread(socket);
        threads.add(echoThread);
        echoThread.start();
    }

    private static void printHelpForCommand(){
        System.out.println("Input Format - connect [ip] [port]");
    }

    private static void printHelpForArgs(){
        System.out.println("Input Args - [port]");
    }
}
