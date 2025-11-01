package com.arsw.tictac.services;

import com.arsw.tictac.entity.GameRoom;
import com.arsw.tictac.repository.GameRoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameRoomService {
    private final GameRoomRepository repository;

    public GameRoomService(GameRoomRepository repository) {
        this.repository = repository;
    }

    public GameRoom createRoom(String name) {
        GameRoom room = new GameRoom();
        room.setName(name);
        room.setCurrentState("---------");
        return repository.save(room);
    }

    public Optional<GameRoom> getRoom(Long id) {
        return repository.findById(id);
    }

    public GameRoom updateRoom(GameRoom room) {
        return repository.save(room);
    }
}