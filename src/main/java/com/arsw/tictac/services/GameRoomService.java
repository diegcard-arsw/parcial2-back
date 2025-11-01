package com.arsw.tictac.services;

import com.arsw.tictac.entity.GameRoom;
import com.arsw.tictac.entity.GameState;
import com.arsw.tictac.repository.GameRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class GameRoomService {
    
    @Autowired
    private GameRoomRepository repository;

    public GameRoom createRoom(String name) {
        GameRoom room = new GameRoom();
        room.setName(name);
        room.setPlayerCount(0);
        room.setCurrentPlayer("X");
        
        GameState initialState = new GameState();
        initialState.setSquares(new String[9]);
        initialState.setStepNumber(0);
        room.setHistory(new ArrayList<>(Arrays.asList(initialState)));
        room.setCurrentState(initialState);
        
        return repository.save(room);
    }

    public GameRoom joinRoom(Long roomId) {
        Optional<GameRoom> optionalRoom = repository.findById(roomId);
        if (optionalRoom.isPresent()) {
            GameRoom room = optionalRoom.get();
            if (room.getPlayerCount() < 2) {
                room.setPlayerCount(room.getPlayerCount() + 1);
                return repository.save(room);
            }
        }
        throw new RuntimeException("Room is full or not found");
    }

    public GameRoom makeMove(Long roomId, int squareIndex, String playerSymbol) {
        Optional<GameRoom> optionalRoom = repository.findById(roomId);
        if (optionalRoom.isPresent()) {
            GameRoom room = optionalRoom.get();
            GameState currentState = room.getCurrentState();
            
            String[] squares = Arrays.copyOf(currentState.getSquares(), 9);
            
            if (squares[squareIndex] != null || calculateWinner(squares) != null) {
                return room;
            }
            
            squares[squareIndex] = playerSymbol;
            
            GameState newState = new GameState();
            newState.setSquares(squares);
            newState.setStepNumber(room.getHistory().size());
            
            room.getHistory().add(newState);
            room.setCurrentState(newState);
            room.setCurrentPlayer(playerSymbol.equals("X") ? "O" : "X");
            
            String winner = calculateWinner(squares);
            if (winner != null) {
                room.setWinner(winner);
            }
            
            return repository.save(room);
        }
        throw new RuntimeException("Room not found");
    }

    public GameRoom jumpToStep(Long roomId, int stepNumber) {
        Optional<GameRoom> optionalRoom = repository.findById(roomId);
        if (optionalRoom.isPresent()) {
            GameRoom room = optionalRoom.get();
            if (stepNumber >= 0 && stepNumber < room.getHistory().size()) {
                GameState state = room.getHistory().get(stepNumber);
                room.setCurrentState(state);
                room.setCurrentPlayer(stepNumber % 2 == 0 ? "X" : "O");
                return repository.save(room);
            }
        }
        throw new RuntimeException("Invalid step or room not found");
    }

    public GameRoom leaveRoom(Long roomId) {
        Optional<GameRoom> optionalRoom = repository.findById(roomId);
        if (optionalRoom.isPresent()) {
            GameRoom room = optionalRoom.get();
            room.setPlayerCount(Math.max(0, room.getPlayerCount() - 1));
            return repository.save(room);
        }
        throw new RuntimeException("Room not found");
    }

    public GameRoom updateRoom(GameRoom room) {
        return repository.save(room);
    }

    public Optional<GameRoom> getRoom(Long id) {
        return repository.findById(id);
    }

    private String calculateWinner(String[] squares) {
        int[][] lines = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };
        
        for (int[] line : lines) {
            if (squares[line[0]] != null &&
                squares[line[0]].equals(squares[line[1]]) &&
                squares[line[0]].equals(squares[line[2]])) {
                return squares[line[0]];
            }
        }
        return null;
    }
}