package server.requests;

import server.client.Client;
import server.file.FileHandler;

public class RequestFulfiller extends Thread{
    private boolean fulfillingRequest = false;

    @Override
    public void run() {
        // Start looping
        while(true){
            fulfillingRequest = false;
            Request r = null;
            // get the next request
            try {
                r = RequestQueue.retrieveRequest();
            } catch (InterruptedException i){
                Thread.currentThread().interrupt();
                i.printStackTrace();
            }
            //break if null request
            if (r == null) break;
            // fulfill the request
            fulfillRequest(r);
        }
    }

    private void fulfillRequest(Request r) {
        if (r.getRequestType() == RequestType.SET) {
            // set the value of the key in the database
            try {
                fulfillingRequest = true;
                FileHandler.storeValue(r.getKey(), ((SetRequest) r).getValue());
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("STORED\r\n");
            } catch (Exception e) {
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("NOT-STORED\r\n");
            }


        } else if (r.getRequestType() == RequestType.GET) {
            // get the value of the key from the database
            try {
                fulfillingRequest = true;
                String[] key = FileHandler.getValue(r.getKey());
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("VALUE %s %d \r\n".formatted(r.getKey(), r.getKey().length()));
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("%s \r\n".formatted(key[1]));
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("END\r\n");
            } catch (Exception e) {
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("GET-FAILED\r\n");
            }
        }
    }
}
