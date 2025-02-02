package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.sprint.event.MessageEventHandler;
import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.api.sprint.service.SprintService;

import io.opentracing.Tracer;

@Component
public class CompletedSprintConsumer {

    public final static String TOPIC = "update.sprint.topic";

    @Autowired
    private Tracer tracer;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private MessageEventHandler messageEventHandler;

    @JmsListener(destination = TOPIC, containerFactory = "myFactory")
    public void onMessage(String message, @Header("message.event.interaction.reference") String reference) {
        Sprint sprint = Sprint.fromJSON(message);
        try {
            if (messageEventHandler.isValid(TOPIC, reference) && sprint.getStatus().equals("Completed")) {
                sprintService.completedSprint(sprint.getId());
            }
        } finally {
            if (tracer.activeSpan() != null) {
                tracer.activeSpan().close();
            }
        }
    }

}
