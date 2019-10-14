package TTT;

import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */

    public final int PLAYER_A = 1;
    public final int PLAYER_B = 2;
    public int next_player;
    public int this_player;
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        int depth = 4; //TODO: ÄNDRA SEN
        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }
        int best_prob = Integer.MIN_VALUE;
        int best_move = 0;
        this_player = gameState.getNextPlayer() == PLAYER_A ? PLAYER_B : PLAYER_A;
        next_player = gameState.getNextPlayer();
        for(int i = 0; i < nextStates.size(); i++){

            int current_val = alphabeta(nextStates.get(i), depth, Integer.MIN_VALUE,
                    Integer.MAX_VALUE, this_player);

            if(current_val > best_prob){
                best_move = i;
                best_prob = current_val;
            }
        }

        return nextStates.elementAt(best_move);
    }

    public int alphabeta(GameState state, int depth, int alpha, int beta, int player){
        int v = 0;
        Vector<GameState> my = new Vector<GameState>();
        state.findPossibleMoves(my);

        if((depth == 0) || (my.size() == 0)){
            v = gamma(state);
        }else if(player ==  next_player){
            v = Integer.MIN_VALUE;
            for(GameState child : my){
                v = Math.max(v, alphabeta(child, depth-1, alpha, beta, this_player));
                alpha = Math.max(alpha, v);
                if(alpha >= beta) {
                    break; // beta prune
                }
            }

        }else if(player == this_player){
            v = Integer.MAX_VALUE;
            for(GameState child : my){
                v = Math.min(v, alphabeta(child, depth-1, alpha, beta, next_player));
                beta = Math.min(beta, v);
                if(alpha >= beta) {
                    break; // alfa prune
                }
            }
        }
        return v;
    }


    public int gamma(GameState state){
            if(state.isEOG()) {
                if (state.isXWin()) {
                    return Integer.MAX_VALUE;
                } else if (state.isOWin()) {
                    return Integer.MIN_VALUE;
                }else{
                    return 0;
                }
            }
            int eval = 0;
            //ROWS
            for(int i = 0; i < state.BOARD_SIZE; i++){
                int x = 0;
                int o = 0;
                for(int j = 0; j < state.BOARD_SIZE; j++){
                    if(state.at(i,j) == PLAYER_A){
                        x++;
                    }else if(state.at(i, j) == PLAYER_B){
                        o++;
                    }
                }
                eval += calc_prob(x, o);

            }
            //COLLUMNS
            for(int i = 0; i < state.BOARD_SIZE; i++){
                int x = 0;
                int o = 0;
                for(int j = 0; j < state.BOARD_SIZE; j++){
                    if(state.at(j,i) == PLAYER_A){
                        x++;
                    }else if(state.at(j, i) == PLAYER_B){
                        o++;
                    }
                }
                eval += calc_prob(x, o);

            }
            //DIAGONAL
            for(int i = 0; i < state.BOARD_SIZE/2; i++){
                int x1 = 0;
                int o1 = 0;
                int x2 = 0;
                int o2 = 0;
                for(int j = 0; j < state.BOARD_SIZE; j++){
                    if(i == 0){
                        if(state.at(j,j) == PLAYER_A){
                            x1++;
                        }else if(state.at(j, j) == PLAYER_B) {
                            o1++;
                        }
                    }else{
                        if(state.at(j,state.BOARD_SIZE-j-1) == PLAYER_A){
                            x2++;
                        }else if(state.at(j, state.BOARD_SIZE-j-1) == PLAYER_B){
                            o2++;
                        }
                    }


                }
                eval += calc_prob(x1, o1);
                eval += calc_prob(x2, o2);
            }
            return eval;

    }

    public int calc_prob(int x, int o){
        if((x == 0)){
            return (int) -Math.pow(10, o);
        }else if (o == 0){
            return (int) Math.pow(10, x);
        }
        return 0;
    }
}
