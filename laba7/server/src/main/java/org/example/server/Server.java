package org.example.server;



import org.example.common.data.City;
import org.example.common.request.CommandsRequest;
import org.example.common.response.CommandsResponse;
import org.example.server.base.CollectionManager;
import org.example.server.base.ConsoleReaderThread;
import org.example.server.base.Controller;
import org.example.server.commandIn.Exit;
import org.example.server.data.Coordinates;
import org.example.server.exceptions.IOException;
import org.example.server.data.*;
import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Server {
    private Selector selector;
    private InetSocketAddress address;
    private Set<SocketChannel> session;
    public static final Logger logger = Logger.getGlobal();
    private ExecutorService readPool;
    private ExecutorService processPool;
    private ForkJoinPool writePool;
    private Controller controller = new Controller();
    public Server(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        this.readPool = Executors.newFixedThreadPool(5); // Fixed thread pool for reading
        this.processPool = Executors.newCachedThreadPool(); // Cached thread pool for processing
        this.writePool = new ForkJoinPool(); // ForkJoinPool for writing
    }

    public void start() throws IOException, ClassNotFoundException, java.io.IOException {
        Thread readerThread = new Thread(new ConsoleReaderThread());
        readerThread.start();

        controller.createCommand();
        this.selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(address);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        logger.info("Сервер запущен, ожидает клиентов");
        while (true) {
            this.selector.select();
            Iterator keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();
                keys.remove();
                if (!key.isValid()) continue;
                if (key.isAcceptable()) accept(key);
                if (key.isReadable()) read(key);
            }
        }
    }

    private void accept(SelectionKey key) throws IOException, java.io.IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);
        this.session.add(channel);
        logger.info("К серверу подключился новый клиент " + channel.socket().getRemoteSocketAddress());
    }

    private void read(SelectionKey key) {
        readPool.submit(() -> {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
            int numRead;
            try {
                numRead = channel.read(byteBuffer);
                if (numRead == -1) {
                    throw new SocketException("Client disconnected");
                }
            } catch (IOException e) {
                synchronized (session) {
                    session.remove(channel);
                }
                logger.info("Вышел клиент: " + channel.socket().getRemoteSocketAddress());
                try {
                    channel.close();
                } catch (IOException ex) {
                    // Handle exception
                } catch (java.io.IOException ex) {
                    throw new RuntimeException(ex);
                }
                key.cancel();
                return;
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            byteBuffer.flip();
            ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer.array(), 0, numRead);
            ObjectInputStream ois = null;
            CommandsRequest request = null;
            try {
                ois = new ObjectInputStream(bis);
                request = (CommandsRequest) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                // Handle exception
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                    bis.close();
                } catch (IOException e) {
                    // Handle exception
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (request != null) {
                processRequest(channel, request);
            }
        });
    }

    private void processRequest(SocketChannel channel, CommandsRequest request) {
        processPool.submit(() -> {
            if (request.getLine()!=null && request.getCity()!=null){
                logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+request.getName()+", её аргумент: "+request.getLine()+", а также город: "+request.getCity().toString());


            } else if (request.getLine()!=null) {
                logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+request.getName()+", её аргумент: "+request.getLine());

            } else if (request.getCity()!=null) {
                logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+request.getName()+", а также город: "+request.getCity().toString());


            }else {
                logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+request.getName());

            }
            Controller controller = new Controller();
            org.example.server.data.City city_new = null;
            try {
                Coordinates coordinates = new Coordinates(request.getCity().getCoordinates().getX(), request.getCity().getCoordinates().getY());
                city_new = new org.example.server.data.City(request.getCity().getId(), request.getCity().getName(),
                        coordinates, LocalDateTime.now(), request.getCity().getArea(), request.getCity().getPopulation(),
                        request.getCity().getMetersAboveSeaLevel(), request.getCity().isCapital(),
                        request.getCity().getPopulationDensity(), Climate.valueOf(request.getCity().getClimate().toString()),
                        new Human(LocalDateTime.now()), request.getCity().getUser_id());
            } catch (NullPointerException ignored) {
            }

            String result = null;
            try {
                result = controller.run(request.getName(), city_new, request.getLine(), request.getUser(), request.getPassword(), request.getId());


            }catch (Exception e){

            }

            CommandsResponse response = new CommandsResponse(request.getName(), null, result,
                    CollectionManager.user_id, CollectionManager.authoriz);
            sendResponse(channel, response);
        });
    }

    private void sendResponse(SocketChannel channel, CommandsResponse response) {
        writePool.submit(() -> {
            try {
                logger.info("Сервер отправил клиенту <"+channel.socket().getRemoteSocketAddress()+"> сообщение: "+response.getResult());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(response);
                oos.flush();
                byte[] responseBytes = baos.toByteArray();

                ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
                sizeBuffer.putInt(responseBytes.length);
                sizeBuffer.flip();
                channel.write(sizeBuffer);

                ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
                channel.write(responseBuffer);
            } catch (IOException e) {
                logger.info("Ошибка отправки сообщения клиенту: " + e.getMessage());
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, java.io.IOException {
        Server server = new Server("localhost", 6042);
        server.start();
    }
//


//    private Selector selector;
//    private InetSocketAddress address;
//    private Set<SocketChannel> session;
//    public static final Logger logger = Logger.getGlobal();
//
//    public Server(String host, int port) {
//        this.address = new InetSocketAddress(host, port);
//        this.session = new HashSet<SocketChannel>();
//
//    }
//
//    public void start() throws IOException, java.io.IOException, ClassNotFoundException {
//        Thread readerThread = new Thread(new ConsoleReaderThread());
//        readerThread.start();
//        Controller controller =new Controller();
//        controller.createCommand();
//        this.selector = Selector.open();
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        serverSocketChannel.bind(address);
//        serverSocketChannel.configureBlocking(false);
//        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
//        logger.info("Сервер запущен, ожидает клиентов");
//        while (true) {
//            this.selector.select();
//            Iterator keys = this.selector.selectedKeys().iterator();
//            while (keys.hasNext()) {
//                SelectionKey key = (SelectionKey) keys.next();
//                keys.remove();
//                if (!key.isValid()) continue;
//                if (key.isAcceptable()) accept(key);
//                if (key.isReadable()) read(key);
//            }
//        }
//
//    }
//
//    private void accept(SelectionKey key) throws IOException, java.io.IOException {
//        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
//        SocketChannel channel = serverSocketChannel.accept();
//        channel.configureBlocking(false);
//        channel.register(this.selector, SelectionKey.OP_READ);
//        this.session.add(channel);
//        logger.info("К серверу подключился новый клиент " + channel.socket().getRemoteSocketAddress());
//
//    }
//
//
//    private void read(SelectionKey key) throws IOException, java.io.IOException, ClassNotFoundException {
//        SocketChannel channel = (SocketChannel) key.channel();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
//
//        int numRead;
//        try {
//            numRead = channel.read(byteBuffer);
//        }catch (SocketException e){
//            this.session.remove(channel);
//            logger.info("Вышел клиент: " + channel.socket().getRemoteSocketAddress());
//            channel.close();
//            key.cancel();
//            return;
//        }
//
//        byteBuffer.flip();
//        ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer.array(), 0, numRead);
//
//        ObjectInputStream ois = new ObjectInputStream(bis);
//        Controller controller = new Controller();
//        if (numRead == -1) {
//            logger.info("Клиент отключился: " + channel.socket().getRemoteSocketAddress());
//            this.session.remove(channel);
//            channel.close();
//            key.cancel();
//            return;
//        }
//        CommandsRequest city = (CommandsRequest) ois.readObject();
//        if (city.getLine()!=null && city.getCity()!=null){
//            logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+city.getName()+", её аргумент: "+city.getLine()+", а также город: "+city.getCity().toString());
//
//
//        } else if (city.getLine()!=null) {
//            logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+city.getName()+", её аргумент: "+city.getLine());
//
//        } else if (city.getCity()!=null) {
//            logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+city.getName()+", а также город: "+city.getCity().toString());
//
//
//        }else {
//            logger.info("Сервер получил от клиента <"+channel.socket().getRemoteSocketAddress()+"> команду: "+city.getName());
//
//        }
//        ois.close();
//        bis.close();
//
//
//
//
//
//
//        org.example.server.data.City city_new = null;
//
//        try {
//            Coordinates coordinates = new Coordinates(city.getCity().getCoordinates().getX(), city.getCity().getCoordinates().getY());
//            city_new = new org.example.server.data.City(city.getCity().getId(), city.getCity().getName(), coordinates, LocalDateTime.now(), city.getCity().getArea(), city.getCity().getPopulation(), city.getCity().getMetersAboveSeaLevel(), city.getCity().isCapital(), city.getCity().getPopulationDensity(), Climate.valueOf(city.getCity().getClimate().toString()), new Human(LocalDateTime.now()),city.getCity().getUser_id());
//        } catch (NullPointerException e) {
//
//        }
//
//        String result = controller.run(city.getName(), city_new,city.getLine(), city.getUser(),city.getPassword(), city.getId());
//
//        CommandsResponse commandsResponse = new CommandsResponse(city.getName(), null, result, CollectionManager.user_id,CollectionManager.authoriz);
//
//        try {
//
//
//
//
//            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
//            ObjectOutputStream oos1 = new ObjectOutputStream(baos1);
//            oos1.writeObject(commandsResponse);
//            oos1.flush();
//            byte[] responseBytes1 = baos1.toByteArray();
//            int responseSize = responseBytes1.length;
//
//            // Отправка размера данных
//            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
//            sizeBuffer.putInt(responseSize);
//            sizeBuffer.flip();
//            channel.write(sizeBuffer);
//            oos1.close();
//            baos1.close();
//
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(commandsResponse);
//            oos.flush();
//            byte[] responseBytes = baos.toByteArray();
//            ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
//            channel.write(responseBuffer);
//        } catch (IOException e) {
//            logger.info("Ошибка отправки сообщения клиенту: "+e.getMessage());
//
//        }
//        logger.info("Сервер отправил клиенту <"+channel.socket().getRemoteSocketAddress()+"> сообщение: "+commandsResponse.getResult());
//
//    }
//    public static ByteBuffer toBuffer(CommandsResponse object) throws  java.io.IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(object);
//        oos.flush();
//        byte[] data = baos.toByteArray();
//        int length = data.length + 4;
//        ByteBuffer writeBuffer = ByteBuffer.allocate(length);
//        writeBuffer.putInt(data.length);
//        writeBuffer.put(data);
//        return writeBuffer;
//    }
}



