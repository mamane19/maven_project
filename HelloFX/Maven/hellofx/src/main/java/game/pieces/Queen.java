package game.pieces;

import game.*;

import java.util.ArrayList;

/**
 * Author: Bello Moussa
 */
public class Queen extends Piece{
    public Queen(boolean white){
        super(Type.Queen, white);
    }

    public Queen(Piece piece){
        this(piece.getColor());
    }

    /**
     * Function to check if the move being made is following the rules of the corresponding piece(Queen)
     * @param game game variable that stores the piece positions, accessed with game class getPiece()
     * @param from coordinate variable of the pieces starting position
     * @param dx value of delta x
     * @param dy value of delta y
     * @param adx absolute value of delta x
     * @param ady absolute value of delta y
     * @return If move is valid
     */
    @Override
    public boolean isValid(Game game, Move move){
        if(!super.isValid(game, move)) return false;

        Cord from = move.from();
        int adx = move.adx();
        int ady = move.ady();
        int modX = move.dx() > 0? Constant.POSITIVE : Constant.NEGATIVE;
        int modY = move.dy() > 0? Constant.POSITIVE : Constant.NEGATIVE;

        if(adx != ady && adx != 0 && ady != 0) return false;

        //Checks for vertical movement if none returns false
        if (ady == 0){
            for(int i = 1; i < adx; i++)
                if(game.getPiece(new Cord(from.rank(), from.file() + i * modX)).getType() != Type.Empty)
                    return false;
        }
        //Checks for horizontal movement if none returns false
        else if (adx == 0){ 
            for(int i = 1; i < ady; i++)
                if(game.getPiece(new Cord(from.rank() + i * modY, from.file())).getType() != Type.Empty)
                    return false;
        }
        //Checks for diagonal movement if none returns false
        else{
            for(int i = 1; i < adx; i++) // dx == dy
                if(game.getPiece(new Cord(from.rank() + i * modY, from.file() + i * modX)).getType() != Type.Empty)
                    return false;
        }
        return true;
    }

    @Override
    public void updateValue(){
        value = Constant.QUEEN_VALUE;
    }

    @Override
    public void updateValue(Game game, Cord at){
        value = Constant.QUEEN_VALUE;
        value += validMoves(game, at).size() * Constant.QUEEN_SCOPE;
    }

    /**
     * Function to check the moves available to the corrosponding piece(Queen)
     * @param game game variable that stores the piece positions, accessed with game class getPiece()
     * @param from coordinate variable of the pieces starting position
     * @param getRankSize gets the row of the corrosponding piece
     * @param getFileSize gets the column of the corrosponding piece
     * @return an ArrayList of moves that the piece can make
     */
    @Override
    public ArrayList<Cord> validMoves(Game game, Cord from){
        ArrayList<Cord> moves = new ArrayList<Cord>();
        Cord test;

        //rook movement
        for(int i = 0; i < 2; i++){
            int mod = i == 0 ? Constant.POSITIVE : Constant.NEGATIVE;

            for(int k = 1; k < game.getRankSize() || k < game.getFileSize(); k++){
                test = new Cord(from.rank() + (k * mod), from.file());
                if(isValid(game, new Move(from, test)))
                    moves.add(test);
                    
                test = new Cord(from.rank(), from.file() + (k * mod));
                if(isValid(game, new Move(from, test)))
                    moves.add(test);
            }
        }

        //Bishop movement
        for(int i = 0; i < 2; i++)
            for(int j = 0; j < 2; j++){
                int modx = i == 0 ? Constant.POSITIVE : Constant.NEGATIVE;
                int mody = j == 0 ? Constant.POSITIVE : Constant.NEGATIVE;

                for(int k = 1; k < game.getRankSize() && k < game.getFileSize(); k++){
                    test = new Cord(from.rank() + (k *modx), from.file() + (k * mody));
                    if(isValid(game, new Move(from, test)))
                        moves.add(test);
                }
            }

        return moves;
    }

    @Override
    public char toCharacter(){
        return isWhite? 'Q' : 'q';
    }
}
