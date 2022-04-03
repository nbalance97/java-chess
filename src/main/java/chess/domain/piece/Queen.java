package chess.domain.piece;

import chess.domain.board.position.Position;
import java.util.List;
import java.util.function.BiPredicate;

public class Queen extends UnpromotablePiece {

    static final String SYMBOL = "q";
    private static final double SCORE = 9;

    private static final BiPredicate<Integer, Integer> movingCondition =
            Bishop.movingCondition.or(Rook.movingCondition);

    public Queen(final Team team) {
        super(team);
    }

    public boolean canMove(final Position sourcePosition,
                           final Position targetPosition,
                           final List<Position> otherPositions) {
        return sourcePosition.canMove(targetPosition, movingCondition) &&
                !sourcePosition.isOtherPieceInPathToTarget(targetPosition, otherPositions);
    }

    @Override
    public String getSymbol() {
        if (team.isBlack()) {
            return SYMBOL.toUpperCase();
        }
        return SYMBOL;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public double getScore() {
        return SCORE;
    }
}
