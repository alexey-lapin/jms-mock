package jmsmock.domain;


import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeType {

    RECEIVER("receiver", true),
    SENDER("sender", false),
    GROOVY("groovy", true),
    MESSAGE_CREATOR("message-creator", true),
    LOGGER("logger", false);

    private final String name;

    private final boolean isTrigger;

    NodeType(String name, boolean isTrigger) {
        this.name = name;
        this.isTrigger = isTrigger;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public boolean isTrigger() {
        return isTrigger;
    }

}
