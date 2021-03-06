package game.pieces;

import game.*;

import java.util.ArrayList;

/**
 * Author: Bello Moussa
 */
public abstract class Piece{
    protected boolean isWhite;
    private Type type;
    protected double value;

    public Piece(Type type, boolean white){
        isWhite = white;
        this.type = type;
        updateValue();
    }

    public Piece(){
        this(Type.Empty, false);
    }

    public Piece(Piece piece){
        this(piece.getType(), piece.getColor());
    }

    /**
     * Checks that the piece to be moved is the same colour as the player whose turn it is,
     * and that the piece moving is not moving to a square with a same of the same colour.
     * @param game game variable that stores the piece positions, accessed with game class getPiece()
     * @param from coordinate variable of the pieces starting position
     * @param to coordinate variable of the pieces end position
     *
     */
    public boolean isValid(Game game, Move move){
        if(move.from().rank() < 0//move this check to Move class
            || move.from().file() < 0
            || move.to().rank() < 0
            || move.to().file() < 0
            || move.from().rank() > game.getRankSize() - 1
            || move.from().file() > game.getFileSize() - 1
            || move.to().rank() > game.getRankSize() - 1
            || move.to().file() > game.getFileSize() - 1)
            return false;
        if(game.getPiece(move.from()) == null) 
            return false;
        if(game.getPiece(move.from()).getColor() != game.getWhiteTurn()) 
            return false;
        if(game.getPiece(move.to()).getType() != Type.Empty 
            && game.getPiece(move.from()).getColor() == game.getPiece(move.to()).getColor()) 
            return false;

        return true;
    }

    /**
     * Returns a list of all legal moves that a piece can make
     * @param game the current game
     * @param from the coordinates that a piece is moving from
     * @return
     */
    public ArrayList<Cord> legalMoves(Game game, Cord from){
        ArrayList<Cord> moves = validMoves(game, from);
        
        int index = 0;
        while(index < moves.size()){
            if(!isLegal(game, new Move(from, moves.get(index))))//shit logic
                moves.remove(index);
            else
                index++;
        }
        return moves;
    }

    /**
     * Checks to see if a move is legal.  Checks with the piece to checks it's move rules
     * @param game the current game
     * @param move the move that the piece is trying to make
     * @return
     */
    public boolean isLegal(Game game, Move move){
        if(move.to().getPromotion() == '*') return true;//cheat mode
        
        if(GameHelper.sucide(game, move)) return false;
        
        //for castle
        if(type == Type.King && move.adx() == 2 && move.dy() == 0){
            if(GameHelper.inCheck(game)) return false;
            if(move.dx() == 2){
                if(GameHelper.sucide(game, new Move(move.from(), new Cord(move.from().rank(), move.from().file() + 1))))
                    return false;
            }else if(move.dx() == -2){
                if(GameHelper.sucide(game, new Move(move.from(), new Cord(move.from().rank(), move.from().file() - 1))))
                    return false;
            }
        }

        return isValid(game, move);
    }

    public abstract void updateValue(); //Default value of a piece
    public abstract void updateValue(Game game, Cord at);

    /**
     * Function to check the moves avaliable to the corrosponding pieces
     * @param game game variable that stores the piece positions, accessed with game class getPiece()
     * @param from coordinate variable of the pieces starting position
     * @return an ArrayList of moves that the piece can make
     */
    public abstract ArrayList<Cord> validMoves(Game game, Cord from);

    public boolean getColor() {return isWhite;}
    public Type getType() {return type;}
    public double getValue() {return value;}

    public String toString(){
        return "" + type + ", " + isWhite;
    }

    public char toCharacter(){
        return  ' ';
    }
}
