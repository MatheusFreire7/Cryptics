package br.unicamp.appcryptics;

public class MessageModel {
    String uId, message, messageId, image;
    long dataMensagem;


    public MessageModel(String uId, String message, String messageId, String image) {
        this.uId = uId;
        this.message = message;
        this.messageId = messageId;
        this.image = image;
    }

    public MessageModel(String uId, String message, long dataMensagem) {
        this.uId = uId;
        this.message = message;
        this.dataMensagem = dataMensagem;
    }

    public MessageModel(String uId, String message, String image) {
        this.uId = uId;
        this.message = message;
        this.image = image;
    }

    public MessageModel(String uId, String message, String messageId, String image, long dataMensagem) {
        this.uId = uId;
        this.message = message;
        this.messageId = messageId;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
