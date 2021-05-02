package jmsmock.domain.model;


import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeType {

    COMPOSER("composer", true),
    DELAY("delay", false),
    GROOVY("groovy", true),
    LOGGER("logger", false),
    RECEIVER("receiver", true),
    SENDER("sender", false);

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
