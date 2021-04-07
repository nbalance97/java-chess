package domain.piece;

import domain.board.Score;
import domain.position.Direction;
import domain.position.Position;
import java.util.Map;

public class King extends Piece {

    private static final String NAME = "k";
    private static final Score SCORE = new Score(0);

    public King(Color color) {
        super(NAME, color, SCORE);
    }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public boolean canMove(Map<Position, Piece> board, Position source, Position target) {
        return Direction.everyDirection()
            .stream()
            .anyMatch(direction -> source.sum(direction).equals(target));
    }

}