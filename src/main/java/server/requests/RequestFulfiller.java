package server.requests;

import server.client.Client;
import server.file.FileHandler;

public class RequestFulfiller extends Thread{

    @Override
    public void run() {

        // while there are requests to fulfill
        while(!RequestQueue.isNoRequests()){
            // get the next request
            Request r = RequestQueue.retrieveRequest();
            // fulfill the request
            fulfillRequest(r);
        }
    }

    private void fulfillRequest(Request r) {
        if (r.getRequestType() == RequestType.SET) {
            // set the value of the key in the database
            try {
                FileHandler.storeValue(r.getKey(), ((SetRequest) r).getValue());
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("STORED\r\n");
            } catch (Exception e) {
                Client.getClientById(r.getClientId()).getClientHandler().outputMessage("NOT-STORED\r\n");
            }


        } else if (r.getRequestType() == RequestType.GET) {
            // get the value of the key from the database
            try {
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
