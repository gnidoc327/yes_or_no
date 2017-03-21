package udp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class MsgReceiver {
    static int receiverPort = 6666;
    static ServerSocket welcomeSocket = null;
    static Socket receiverSocket = null;
    static DataInputStream inFromSender = null;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket datagramPacket;

    public static void main(String[] args) {
		if ( args.length != 1 ){
            printHelp();
		}

		//parsing for port number at args
        receiverPort = Integer.parseInt(args[0]);

        //start server for protocol
        System.out.println("Receiver: UDP Server start");

        runUdpServer();
    }

    private static void printHelp() {
        System.err.println("MsgReceiver  <receiver port>");
        System.err.println("where  <receiver port> is the port number at which MsgReceiver is listening");
    }

    private static void runUdpServer(){
        try {
            // wait for a connection request from MsgSender
            datagramSocket = new DatagramSocket(receiverPort);
        } catch (IOException e) {
            System.out.println("Receiver: Server Socket Exception");
            e.printStackTrace();
        }

        String sentence = null;

        while(true){
            try {
                byte[] bytes = new byte[1024];
                datagramPacket = new DatagramPacket(bytes, bytes.length);
                datagramSocket.receive(datagramPacket);
                sentence = new String(datagramPacket.getData());
                System.out.println(sentence);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
