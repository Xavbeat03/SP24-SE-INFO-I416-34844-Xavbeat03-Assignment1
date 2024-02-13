package server.requests;

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

            } catch (Exception e) {

            }


        } else if (r.getRequestType() == RequestType.GET) {
            // get the value of the key from the database
            // ...
        }
    }
}
