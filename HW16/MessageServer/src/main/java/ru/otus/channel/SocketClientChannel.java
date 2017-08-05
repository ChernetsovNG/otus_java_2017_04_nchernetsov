package ru.otus.channel;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.app.Message;
import ru.otus.app.MessageChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientChannel implements MessageChannel {
    private static final Logger logger = Logger.getLogger(SocketClientChannel.class.getName());
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket client;
    private final List<Runnable> shutdownRegistrations;

    public SocketClientChannel(Socket client) {
        this.client = client;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        this.shutdownRegistrations = new CopyOnWriteArrayList<>();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    public void addShutdownRegistration(Runnable runnable) {
        this.shutdownRegistrations.add(runnable);
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {  // blocks here
                //System.out.println("Message received: " + inputLine);
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty() && !stringBuilder.toString().isEmpty()) {
                    String json = stringBuilder.toString();
                    Message msg = getMsgFromJSON(json);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException | ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Message getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(Message.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (Message) new Gson().fromJson(json, msgClass);
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            while (client.isConnected()) {
                Message message = output.take();  // blocks here
                String json = new Gson().toJson(message);
                out.println(json);
                out.println("\n");
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void send(Message message) {
        output.add(message);
    }

    @Override
    public Message poll() {
        return input.poll();
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    @Override
    public void close() throws IOException {
        shutdownRegistrations.forEach(Runnable::run);
        shutdownRegistrations.clear();

        executor.shutdown();
    }
}
