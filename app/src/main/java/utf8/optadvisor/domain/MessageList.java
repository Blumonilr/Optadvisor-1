package utf8.optadvisor.domain;

import java.util.List;

import utf8.optadvisor.domain.entity.Message;

public class MessageList {
    private List<Message> read;
    private List<Message> unread;

    public List<Message> getRead() {
        return read;
    }

    public void setRead(List<Message> read) {
        this.read = read;
    }

    public List<Message> getUnread() {
        return unread;
    }

    public void setUnread(List<Message> unread) {
        this.unread = unread;
    }
}
