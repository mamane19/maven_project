/**
 * An AI that uses a minimax strategy to decide it's move.
 * This AI considers a given number of future moves, which is set in
 * the CONSTANT class.
 */
package ai;

import game.*;

import java.util.ArrayList;
/**
 * Author: Serge Anan
 */
public class AIMinMax extends Player{
    public static int positions = 0;

    public AIMinMax(Game game) {
        super(game);
        kind = Intelligence.MinMax;
    }

    @Override
    public boolean move(){
        positions = 0;
        Move move = minMax();
        if(move != null) System.out.println("Move made: " + move.toString() + ", Debug: " + move.getValue());
        if(GameHelper.legalMove(game, move))
            makeMove(move);
        System.out.println("MinMax calculated " + positions + " positions at depth " + Constant.DEFAULT_MINMAX);
        return true;
    }

    public Move minMax(){return minMax(Constant.DEFAULT_MINMAX);}

    public Move minMax(int depth){
        if(depth < 1) return null;

        ArrayList<Move> legalMoves = GameHelper.allLegalMoves(game);
        
        if(depth == 1){
            for(Move move : legalMoves){
                Game tempGame = new Game(game);
                tempGame.move(move);
                move.setValue(tempGame.getAdvantage());
                positions++;
            }
        }else{
            for(Move move : legalMoves){
                Game tempGame = new Game(game);
                tempGame.move(move);
                AIMinMax tempAI = new AIMinMax(tempGame);
                move.setValue(tempAI.minMax(depth - 1).getValue());
                positions++;
            }
        }
        return game.getWhiteTurn()? max(legalMoves) : min(legalMoves);
    }
}