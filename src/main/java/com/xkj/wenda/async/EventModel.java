package com.xkj.wenda.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType type;
    private int actorId;
    private int entityType;//表示载体类型。如评论的是那个问题，给哪个评论点的赞
    private int entityId;
    private int entityOwnerId;

    private Map<String, String> exts = new HashMap<>();//扩展字段

    public EventModel() {
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;//为了调用方便，可以实现链式调用方法：A.setExt().setExt()...
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

}

























