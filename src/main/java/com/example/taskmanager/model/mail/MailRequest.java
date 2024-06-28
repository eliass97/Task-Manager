package com.example.taskmanager.model.mail;

public class MailRequest {

    private String to;
    private String from;
    private String subject;
    private String text;

    public MailRequest() {
    }

    public MailRequest(String to, String from, String subject, String text) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MailRequest{" +
                "to=" + to +
                ", from=" + from +
                ", subject=" + subject +
                ", text=" + text +
                "}";
    }
}
