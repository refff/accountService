package account.domain.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.context.ApplicationEvent;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String date;
    private String action;
    private String subject;
    private String object;
    private String path;

    public Event() {
    }

    public Event(String path, String object, String subject, String action, String date) {
        this.path = path;
        this.object = object;
        this.subject = subject;
        this.action = action;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static class Builder {
        private String date;
        private String action;
        private String subject;
        private String object;
        private String path;

        public Builder() {
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setAction(String action) {
            this.action = action;
            return this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }
        public Builder setObject(String object) {
            this.object = object;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Event build() {
            return new Event(path, object, subject, action, date);
        }
    }
}
