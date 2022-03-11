package com.dstz.base.core.event;


import com.dstz.base.api.constant.EventEnum;
import org.springframework.context.ApplicationEvent;

public class CommonEvent extends ApplicationEvent {
    EventEnum eventEnum;

    public CommonEvent(EventEnum eventEnum) {
        super("");
        this.setEventEnum(eventEnum);
    }

    public CommonEvent(Object source) {
        super(source);
    }

    public CommonEvent(Object source, EventEnum eventEnum) {
        super(source);
        this.eventEnum = eventEnum;
    }

    public EventEnum getEventEnum() {
        return this.eventEnum;
    }

    public void setEventEnum(EventEnum eventEnum) {
        this.eventEnum = eventEnum;
    }
}