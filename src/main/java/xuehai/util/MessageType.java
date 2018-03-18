package xuehai.util;

public enum MessageType {
    FOLLOW(0),
    PUBLISH(1),
    COLLECTION(2),
    ANSWER(3),
    LIKE(4),
    COMMENT(5);



    private final int value;
    MessageType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
