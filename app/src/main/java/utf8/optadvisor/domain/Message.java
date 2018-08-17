package utf8.optadvisor.domain;

public class Message {
    private String title;
    private String time;
    private String content;

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
