package tcp;

import java.io.IOException;
import java.io.DataOutputStream;
import java.net.*;
import java.util.Scanner;

import static java.lang.Thread.sleep;


public class MsgSender {
    static String receiverHost = "localhost";
    static InetAddress receiverIP;
    static int receiverPort = 6666;

    static DataOutputStream outToReceiver;
    static Socket socket;

    public static void main(String[] args) {
        if (args.length == 2) {
            receiverHost = args[0];
            receiverPort = Integer.parseInt(args[1]);
        } else if (args.length == 1)
            // MsgReceiver is at 'localhost'
            receiverPort = Integer.parseInt(args[0]);
        else {
            printHelp();
            return;
        }

        // Get the IPaddress of Receiver Host
        try {
            InetAddress[] inetAddresses = InetAddress.getAllByName(receiverHost);
            System.out.println(receiverHost + " name list");
            for (InetAddress inetaddress : inetAddresses) {
                System.out.println(inetaddress);
            }
        } catch (UnknownHostException uhe) {
            System.err.println("unknown host: " + receiverHost);
            return;
        }

        // connect a socket to the receiver
        try {
            socket = new Socket(receiverHost, receiverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // set up a socket stream and keyin stream
        System.out.println("Input some text");
        Scanner inFromUser = new Scanner(System.in);
        try {
            outToReceiver = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                String sentence = inFromUser.nextLine();
                outToReceiver.writeUTF(sentence+'\n');
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    private static void printHelp() {
        System.err.println("MsgSender [<receiver host>] <receiver port> ");
        System.err.println("where <receiver port> is the port number at which MsgReceiver is listening");
        System.err.println("         <receiver host> is the host name or IP address where MsgReceiver is running(default: localhost) ");
    }
}


