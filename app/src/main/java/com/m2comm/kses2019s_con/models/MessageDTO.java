package com.m2comm.kses2019s_con.models;

public class MessageDTO {

    private String subject;
    private String content;

    public MessageDTO(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
