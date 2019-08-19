package br.com.developen.sig.exception;

import br.com.developen.sig.util.Messaging;

public class TaskException extends Exception implements Messaging {

    private String[] messages;

    public TaskException(String[] messages){

        this.messages = messages;

    }

    public String[] getMessages(){

        return this.messages;

    }

}