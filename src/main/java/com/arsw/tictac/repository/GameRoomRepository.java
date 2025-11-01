package com.arsw.tictac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arsw.tictac.entity.GameRoom;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
}