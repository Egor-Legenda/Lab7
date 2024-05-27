package org.example.Client;

import org.example.Client.base.CollectionManager;
import org.example.Client.base.CommandControl;
import org.example.Client.base.Controller;
import org.example.Client.commandIn.Exit;
import org.example.common.data.*;
import org.example.common.request.CommandsRequest;
import org.example.common.request.Request;
import org.example.common.response.CommandsResponse;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class Main {
//    public static int PORT = 2024;
//    private static ExecutorService readPool;
//    private static ExecutorService processPool;
//    private static ForkJoinPool writePool;
//
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        readPool = Executors.newFixedThreadPool(5);
//        processPool = Executors.newCachedThreadPool();
//        writePool = new ForkJoinPool();
//
//        try (SocketChannel channel = SocketChannel.open()) {
//            channel.connect(new InetSocketAddress("localhost", 6042));
//            channel.configureBlocking(false);
//            Controller controller = new Controller();
//            controller.createCommand();
//            System.out.println("Успешное подключение к серверу");
//
//            while (true) {
//                controller.run();
//                if (!"skip".equals(CollectionManager.request.getName())) {
//                    sendRequest(channel, CollectionManager.request);
//                    readResponse(channel);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Не удалось подключиться");
//        } finally {
//            readPool.shutdown();
//            processPool.shutdown();
//            writePool.shutdown();
//        }
//    }
//
//    private static void sendRequest(SocketChannel channel, Object request) {
//        writePool.submit(() -> {
//            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
//                oos.writeObject(request);
//                oos.flush();
//                byte[] requestBytes = baos.toByteArray();
//
//                ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
//                sizeBuffer.putInt(requestBytes.length);
//                sizeBuffer.flip();
//                channel.write(sizeBuffer);
//
//                ByteBuffer requestBuffer = ByteBuffer.wrap(requestBytes);
//                channel.write(requestBuffer);
//            } catch (IOException e) {
//                System.err.println("Ошибка отправки запроса: " + e.getMessage());
//            }
//        });
//    }
//
//    private static void readResponse(SocketChannel channel) {
//        readPool.submit(() -> {
//            try {
//                ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
//                while (channel.read(sizeBuffer) != 4);
//                sizeBuffer.flip();
//                int responseSize = sizeBuffer.getInt();
//
//                ByteBuffer responseBuffer = ByteBuffer.allocate(responseSize);
//                while (channel.read(responseBuffer) != responseSize);
//                responseBuffer.flip();
//
//                processResponse(responseBuffer);
//            } catch (IOException e) {
//                System.err.println("Ошибка чтения ответа: " + e.getMessage());
//            }
//        });
//    }
//
//    private static void processResponse(ByteBuffer responseBuffer) {
//        processPool.submit(() -> {
//            try (ByteArrayInputStream bais = new ByteArrayInputStream(responseBuffer.array());
//                 ObjectInputStream ois = new ObjectInputStream(bais)) {
//                CommandsResponse response = (CommandsResponse) ois.readObject();
//                handleResponse(response);
//            } catch (IOException | ClassNotFoundException e) {
//                System.err.println("Ошибка обработки ответа: " + e.getMessage());
//            }
//        });
//    }
//
//    private static void handleResponse(CommandsResponse message) {
//        System.out.println("Сообщение от сервера: \n" + message.getResult());
//        if ("reg_in".equals(message.getName()) || "log_in".equals(message.getName())) {
//            Controller.authoriz = message.isAdd();
//            Controller.user_id = message.getId();
//        }
//    }
    public static int PORT = 2024;


    private static ExecutorService readPool;
    private static ExecutorService writePool;

    public static void main(String[] args) throws IOException, ClassNotFoundException {



        try {

            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", 6042));
            channel.configureBlocking(false);
            Controller controller = new Controller();
            controller.createCommand();
            System.out.println("Успешное подключение к серверу");
            while (true) {

                controller.run();
                if (CollectionManager.request.getName()!="skip") {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(CollectionManager.request);
                    oos.flush();
                    byte[] requestBytes = baos.toByteArray();
                    ByteBuffer requestBuffer = ByteBuffer.wrap(requestBytes);
                    channel.write(requestBuffer);


                    printer(channel);
                }


            }
        } catch (IOException e) {
            System.out.println("Не удалось подключиться");
        }
    }

    private static boolean printer(SocketChannel channel) throws IOException, ClassNotFoundException {
        while (channel.isOpen()) {
            // Чтение размера данных
            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            int totalBytesRead = 0;
            while (totalBytesRead < 4) {
                int bytesRead = channel.read(sizeBuffer);
                if (bytesRead == -1) {
                    System.out.println("Сервер закрыл соединение.");
                    channel.close();
                    Exit  exit1 =new Exit();
                    exit1.execution("exit");

                    return false;
                }
                totalBytesRead += bytesRead;
            }
            sizeBuffer.flip();
            int responseSize = sizeBuffer.getInt();

            if (responseSize < 0) {
                System.out.println("Некорректный размер данных от сервера");
                channel.close();
                return false;
            }

            // Чтение данных
            ByteBuffer readBuffer = ByteBuffer.allocate(responseSize);
            int remaining = responseSize;
            while (remaining > 0) {
                int bytesRead = channel.read(readBuffer);
                if (bytesRead == -1) {
                    System.out.println("Сервер закрыл соединение.");
                    channel.close();
                    Exit  exit1 =new Exit();
                    exit1.execution("exit");
                    return false;
                }
                remaining -= bytesRead;
                readBuffer.rewind();
            }
            readBuffer.rewind();
            byte[] byteArray1 = new byte[readBuffer.remaining()];
            readBuffer.get(byteArray1);

            ByteArrayInputStream bais1 = new ByteArrayInputStream(byteArray1);
            try (ObjectInputStream ois1 = new ObjectInputStream(bais1)) {
                CommandsResponse message = (CommandsResponse) ois1.readObject();
                System.out.println("Сообщение от сервера: \n" + message.getResult());
                if (message.getName().equals("reg_in") || message.getName().equals("log_in")){
                    Controller.authoriz= message.isAdd();
                    Controller.user_id= message.getId();

                }
                break;
            } catch (EOFException e) {
                System.out.println("Ошибка отправки данных");
            }
        }
        return true;


    }
    public static Serializable fromByteBuffer(ByteBuffer buffer) throws IOException, ClassNotFoundException {

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(bais);

        Serializable response = (Serializable) objectInputStream.readObject();

        objectInputStream.close();
        bais.close();

        return response;
    }
}

