package com.arsw.tictac.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.arsw.tictac.entity.GameRoom;
import com.arsw.tictac.services.GameRoomService;

import java.util.Optional;

@Controller
public class GameController {
    private final GameRoomService service;

    public GameController(GameRoomService service) {
        this.service = service;
    }

    @MessageMapping("/create-room")
    @SendTo("/topic/rooms")
    public GameRoom createRoom(String name) {
        return service.createRoom(name);
    }

    @MessageMapping("/update-room")
    @SendTo("/topic/room")
    public GameRoom updateRoom(GameRoom room) {
        return service.updateRoom(room);
    }

    @MessageMapping("/get-room")
    @SendTo("/topic/room")
    public Optional<GameRoom> getRoom(Long id) {
        return service.getRoom(id);
    }
}