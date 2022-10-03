package br.unicamp.appcryptics;

public class MessageModel {
    String uId, message, messageId;
    long dataMensagem;


    public MessageModel(String uId, String message,long dataMensagem) {
        this.uId = uId;
        this.message = message;
        this.dataMensagem = dataMensagem;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessageModel(){} // construtor vazio

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getDataMensagem() {
        return dataMensagem;
    }

    public void setDataMensagem(long dataMensagem) {
        this.dataMensagem = dataMensagem;
    }
}
