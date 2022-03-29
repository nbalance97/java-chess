package chess.domain.piece;

import chess.domain.Team;
import chess.domain.board.Direction;
import chess.domain.board.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Pawn extends Piece {

    private static final String BLACK_SYMBOL = "P";
    private static final String WHITE_SYMBOL = "p";
    private static final String CAN_NOT_CATCH_ERROR = "폰은 해당 위치의 기물을 잡을 수 없습니다.";
    private static final String INVALID_DIRECTION_MOVEMENT_ERROR = "폰은 해당 위치로 이동할 수 없습니다.";

    public Pawn(final Team team) {
        super(team);
    }

    @Override
    protected String createSymbol(final Team team) {
        if (team.isBlack()) {
            return BLACK_SYMBOL;
        }
        return WHITE_SYMBOL;
    }

    @Override
    public void checkReachable(final Position source, final Position target) {
        List<Direction> directions = new ArrayList<>(Direction.getPawnByTeam(team));
        if (source.isDefaultRow(team)) {
            directions.add(Direction.getDefaultPawnByTeam(team));
        }

        if (!canMove(source, target, directions)) {
            throw new IllegalArgumentException(MOVEMENT_ERROR);
        }
    }

    private boolean canMove(final Position source, final Position target, final List<Direction> directions) {
        return directions.stream()
                .map(source::addDirection)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(target::equals);
    }

    @Override
    public void validateCatch(final Piece targetPiece, final Direction direction) {
        super.validateCatch(targetPiece, direction);
        if (isWrongCatchDirection(targetPiece, direction)) {
            throw new IllegalArgumentException(CAN_NOT_CATCH_ERROR);
        }
        if (isWrongMovementDirection(targetPiece, direction)) {
            throw new IllegalArgumentException(INVALID_DIRECTION_MOVEMENT_ERROR);
        }
    }

    private boolean isWrongMovementDirection(final Piece targetPiece, final Direction direction) {
        return targetPiece.isBlank() && isDiagonalDirection(direction);
    }

    private boolean isDiagonalDirection(Direction direction) {
        return direction == Direction.NE || direction == Direction.SE
                || direction == Direction.SW
                || direction == Direction.NW;
    }

    private boolean isWrongCatchDirection(final Piece targetPiece, final Direction direction) {
        return !targetPiece.isBlank() && isStraightDirection(direction);
    }

    private boolean isStraightDirection(Direction direction) {
        return direction == Direction.N || direction == Direction.NN
                || direction == Direction.S
                || direction == Direction.SS;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public double getScore() {
        return 1;
    }
}
