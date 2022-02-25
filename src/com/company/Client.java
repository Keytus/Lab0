package com.company;

import java.io.*;
import java.net.*;


public class Client {
    private DatagramSocket socket;
    private InetAddress address;
    private final String login;
    private int anotherPort;
    private final int bufSize = 256;
    private final String fileName = "C:\\Users\\User\\Desktop\\ИТиРОД\\Lab0\\History.txt";

    private byte[] buf = new byte[bufSize];
    private byte[] bufAsync = new byte[bufSize];


    public Client(String lgn) {
        login = lgn;

        try(FileReader reader = new FileReader(fileName))
        {
            int character;
            StringBuilder result = new StringBuilder();
            while((character=reader.read())!=-1)
            {
                result.append((char) character);
            }
            String[] strings = result.toString().split("\n");
            for(int i =0;i<strings.length && strings.length != 1;i++)
            {
                System.out.println(strings[i] + ": "+ strings[i+1]);
                i++;
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        int port1 = 4445;
        int port2 = 4446;
        try {
            socket = new DatagramSocket(port1);
            anotherPort = port2;
        } catch (BindException e) {
            try {
                socket = new DatagramSocket(port2);
                anotherPort = port1;
            } catch (SocketException socketException) {
                socketException.printStackTrace();
                System.exit(-1);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        String post;
        String msg = "";

        Thread newThread = new Thread(() -> {
            while (true)
            {
                DatagramPacket packet = new DatagramPacket(bufAsync, bufAsync.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                System.out.println(received);
            }
        });
        newThread.start();

        while (true)
        {
            post = login;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            try {
                msg = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (msg.equals("")) { continue; }
            if (msg.equals("close")) {
                newThread.interrupt();
                close();
                break;
            }
            post +=": " + msg;
            buf = post.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, anotherPort);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.append(login + "\n");
                writer.append(msg + "\n");
                writer.close();
            }
            catch (IOException e) { }

        }
    }

    public void close() {
        socket.close();
    }
}
