package Hex;

public class Listener extends Thread {

    Channel channel;

    String lastMessage;
    boolean newMsg = false;

    Listener(String channel_name) {
        channel = new Channel(channel_name);
    }

    @Override
    public void run() {
        listen();
    }

    private void listen(){
        while(true){
            String msg = channel.getNext();
            if(msg != null){
                lastMessage = msg;
                newMsg = true;
            }
        }
    }

    public String getLast(){
        if(newMsg){
            newMsg = false;
            return lastMessage;
        }
        return null;
    }

}
