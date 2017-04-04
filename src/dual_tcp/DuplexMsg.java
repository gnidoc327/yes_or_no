package dual_tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by gnidoc327 on 2017. 3. 29..
 */
public class DuplexMsg {
    private static final String DEFAULT_ADDRESS = "localhost"; //127.0.0.1
    static String sendIp = DEFAULT_ADDRESS;
    static int receivePort;
    static int sendPort;

    private static ReceiveThread receiveThread;
    private Socket socket;
    private DataOutputStream outToReceiver;
    //    private static SendThread sendThread;

    public static void main(String[] args){
        if(args.length != 1){
            helpPrint();
            return;
        }

        receivePort = Integer.parseInt(args[0]);

        System.out.println("run port is "+receivePort+"\nwait for connect");

        receiveThread = new ReceiveThread(receivePort);
        receiveThread.run();

        //input opponent ip, port
        System.out.println("input [ip] [port]");
        Scanner scanner = new Scanner(System.in);

        String inputString = scanner.nextLine();
        String[] temps = inputString.split(" ");

        sendIp = temps[0];
        sendPort = Integer.parseInt(temps[1]);
        System.out.println(sendIp+":"+sendPort+" - connecting try");

        try {
            Socket socket = new Socket(sendIp, sendPort);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            while(true){
                String string = scanner.nextLine();
                dataOutputStream.writeUTF(string+'\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void helpPrint(){
        System.out.println("[receive port]");
    }

    private static void send(){

    }

    private static class ReceiveThread extends Thread{
        ServerSocket serverSocket;
        Socket socket;
        DataInputStream dataInputStream;
        int port;

        ReceiveThread(int port) {
            this.port = port;
        }

        void waitForConnect() throws IOException {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            InetAddress inetAddress = serverSocket.getInetAddress();
            sendIp = inetAddress.getHostAddress();
            sendPort = serverSocket.getLocalPort();
            System.out.println(sendIp+":"+sendPort+" is connect\n");
        }

        void runServer() throws IOException {
            dataInputStream = new DataInputStream(socket.getInputStream());
            String string;

            while(true){
                string = dataInputStream.readUTF();
                System.out.print(string);
            }
        }

        @Override
        public void run() {
            try {
                waitForConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
