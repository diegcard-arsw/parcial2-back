
package com.arsw.tictac.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "game_states")
public class GameState {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String[] squares;
    private int stepNumber;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String[] getSquares() { return squares; }
    public void setSquares(String[] squares) { this.squares = squares; }
    
    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }
}