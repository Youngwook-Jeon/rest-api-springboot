package com.young.springbootrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class EventModel extends RepresentationModel {

    @JsonUnwrapped
    private Event event;

    public EventModel(Event event) {
        this.event = event;
    }

}
