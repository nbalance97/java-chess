package chess.controller;

import static chess.domain.piece.Team.BLACK;
import static chess.domain.piece.Team.WHITE;

import chess.domain.board.Board;
import chess.domain.board.position.Position;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ChessGame {
    private Board board;

    public void start() {
        if (board != null) {
            throw new IllegalStateException("게임이 이미 시작중입니다.");
        }
        board = new Board();
    }

    public boolean isPlaying() {
        return board != null;
    }

    public void move(final String sourcePosition, final String targetPosition) {
        checkPlaying();
        board = board.movePiece(Position.from(sourcePosition), Position.from(targetPosition));
    }

    public Map<String, Object> toMap() {
        return board.getPieces()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Entry::getValue));
    }

    public Score getScore() {
        checkPlaying();
        return new Score(board.getTotalPoint(WHITE), board.getTotalPoint(BLACK));
    }

    public void end() {
        if (board == null) {
            throw new IllegalStateException("진행중인 게임이 없습니다.");
        }
        board = null;
    }

    private void checkPlaying() {
        if (board == null) {
            throw new IllegalStateException("진행 중인 게임이 없습니다.");
        }
    }

    class Score {
        private final double whiteScore;
        private final double blackScore;

        public Score(final double whiteScore, final double blackScore) {
            this.whiteScore = whiteScore;
            this.blackScore = blackScore;
        }
    }
}
