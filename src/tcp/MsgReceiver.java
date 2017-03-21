package tcp;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class MsgReceiver {
    static int receiverPort = 6666;
    static ServerSocket welcomeSocket = null;
    static Socket receiverSocket = null;
    static DataInputStream inFromSender = null;

    public static void main(String[] args) {
		if ( args.length != 1 ){
            printHelp();
		}

		//parsing for port number at args
        receiverPort = Integer.parseInt(args[0]);

        //start server for protocol
        System.out.println("Receiver: TCP Server start");

        runTcpServer();
    }

    private static void printHelp() {
        System.err.println("MsgReceiver  <receiver port>");
        System.err.println("where  <receiver port> is the port number at which MsgReceiver is listening");
    }

    private static void runTcpServer(){
        // wait for a connection request from MsgSender
        try {
            welcomeSocket = new ServerSocket(receiverPort);
        } catch (IOException e) {
            System.out.println("Receiver: Server Socket Exception");
            e.printStackTrace();
        }

        try {
            receiverSocket = welcomeSocket.accept();
            InetAddress inetAddress = receiverSocket.getInetAddress();
            System.out.println("Server connected from " + inetAddress.getHostAddress());

            inFromSender = new DataInputStream(receiverSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String clientSentence;

        while (true){
            try {
                // read from the socket stream
                clientSentence = inFromSender.readUTF();
                // and print to the console
                System.out.print(clientSentence);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
