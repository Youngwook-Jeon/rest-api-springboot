package com.young.springbootrestapi.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);
        // Location URI 만들기
        WebMvcLinkBuilder linkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = linkBuilder.toUri();
//        EventModel eventModel = new EventModel(event); // RepresentaionModel 객체 생성하여 진행하는 방법
        EntityModel eventModel = EntityModel.of(newEvent); // EntityModel 객체 활용하는 방법
        eventModel.add(linkTo(EventController.class).withRel("query-events"));
        eventModel.add(linkBuilder.withSelfRel());
        eventModel.add(linkBuilder.withRel("update-event"));
//        return ResponseEntity.created(createdUri).body(eventModel);
        return ResponseEntity.created(createdUri).body(eventModel);
    }
}
