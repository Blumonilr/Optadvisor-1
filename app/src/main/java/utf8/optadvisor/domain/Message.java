package utf8.optadvisor.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable{
    private Long id;

    private String username;

    private Boolean readStatus;

    private String message;

    private String title;

    private String time;

    public Message(String username,String message){
        readStatus=false;
        this.username=username;
        this.message=message;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        this.time = dateFormat.format(date);
    }

    public Message(){}

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public boolean getReadStatus() { return readStatus; }

    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }
}
