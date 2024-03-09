package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This class provides static methods to handle file operations.
 */
public class FileHandler {

    // TODO Properly implement the queue

    // TODO Figure out how information is properly returned to the ClientHandler

    // TODO Return information via Return statement

    // Some kind of wait until the receive or wait until the info is returned?

    // The path to the file
    private static String filePath = "./data/map.txt";
    // Private constructor to prevent instantiation
    private FileHandler(){}
    // A flag to indicate whether the class is in testing mode
    private static boolean testingMode = false;

    private static BlockingQueue<Request> requestBlockingQueue = new ArrayBlockingQueue<>(30, true);

    private static abstract class Request{
        public abstract String getKey() ;

    }

    public static class GetRequest extends Request{
        private String key;
        public GetRequest(String Key){
            this.key = Key;
        }

        public String getKey() {
            return key;
        }

    }

    public static class SetRequest extends Request{
        private String key;
        private String value;
        public SetRequest(String Key, String value){
            this.key = Key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }

    /**
     * Adds a request to the queue
     * @param r the request to add to the queue
     */
    public static void addRequest(Request r){
        requestBlockingQueue.add(r);
    }

    public static String handleRequest(Request r){
        while(true) {
            if (requestBlockingQueue.peek().equals(r)) {
                try {
                    Request e = requestBlockingQueue.take();
                    if(e instanceof SetRequest s){

                        if (storeValue((s).key, (s).value)){
                            return "STORED\r\nEND\r\n";
                        } else {
                            return "NOT-STORED\r\nEND\r\n";
                        }

                    } else if (e instanceof GetRequest g){
                        String[] values = getValue(g.key);
                        if(values.length == 2){
                            return "VALUE %s %d \r\n%s\r\nEND\r\n".formatted(values[0], values[1].length(), values[1]);
                        } else if(values.length == 1) {
                            return "VALUE %s 0\r\n\r\nEND\r\n".formatted(values[0]);
                        } else {
                            return "END\r\n";
                        }
                    } else {
                        throw new IllegalArgumentException("Request r was of an illegal request type");
                    }
                } catch (InterruptedException i) {
                     Thread.currentThread().interrupt();
                     i.printStackTrace();
                     continue;
                }

            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
                i.printStackTrace();
            }
        }

    }

    /**
     * This method stores a key-value pair in the file.
     * If the key already exists in the file, it replaces the old value with the new one.
     * If the key does not exist, it appends the key-value pair to the end of the file.
     *
     * @param value The value to be stored
     * @param key The key associated with the value
     */
    private static synchronized boolean storeValue(String value, String key){
        if(value.isEmpty() || key.isEmpty()) throw new IllegalArgumentException("Key or Value is empty.");
        sleepForRandomShortDuration();
        try(BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()))){
            String line = fileReader.readLine();
            StringBuilder totalFile = new StringBuilder();
            while(line!=null){
                if(line.split(",")[0].equals(key)){
                    totalFile.append("%s,%s%n".formatted(key,value));
                } else {
                    totalFile.append((line + "%n").formatted());
                }
                line = fileReader.readLine();
            }
            if(getValue(key).length == 0){
                totalFile.append("%s,%s%n".formatted(key, value));
            }
            writeFile(String.valueOf(totalFile));
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

	}

    /**
     * This method writes a string to the file.
     *
     * @param totalFile The string to be written to the file
     */
    private static synchronized void writeFile(String totalFile) {
        try (FileWriter fileWriter = new FileWriter(getFilePath())) {
            fileWriter.write(totalFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves the value associated with a given key from the file.
     * If the key is not found, it returns an empty array.
     *
     * @param key The key whose associated value is to be returned
     * @return The value associated with the key, or an empty array if the key is not found
     */
    private static synchronized String[] getValue(String key){
        sleepForRandomShortDuration();
        try(BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()))){
            String line = fileReader.readLine();
            while(line!=null){
                if(line.split(",")[0].equals(key))return line.split(",");
                line = fileReader.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return new String[]{};
    }

    /**
     * This method returns the file path.
     *
     * @return The file path
     */
    private static String getFilePath(){return filePath;}

    /**
     * This method sets the file path.
     *
     * @param filePath1 The new file path
     */
    private static void setFilePath(String filePath1) {
        filePath = filePath1;
    }

    /**
     * This method causes the current thread to sleep for a random short duration.
     * It does nothing if the class is in testing mode.
     */
    private static void sleepForRandomShortDuration(){
        if(testingMode) return;
        try {
            Random random = new Random();
            TimeUnit.MILLISECONDS.sleep(
                    random.nextInt(1000)
            );
        } catch (InterruptedException i){
            i.printStackTrace();
        }
    }

    /**
     * This method sets the testing mode.
     *
     * @param b The new testing mode
     */
    public static void setTestingMode(boolean b){
        testingMode = b;
    }

}
