package chess.domain.board;

import chess.domain.piece.Team;
import java.util.List;

public enum Direction {
	S(-1, 0),
	W(0, -1),
	N(1, 0),
	E(0, 1),
	SW(-1, -1),
	SE(-1, 1),
	NW(1, -1),
	NE(1, 1),
	NNE(2, 1),
	NNW(2, -1),
	SSE(-2, 1),
	SSW(-2, -1),
	EEN(1, 2),
	EES(-1, 2),
	WWN(1, -2),
	WWS(-1, -2);

	private final int rowMovement;
	private final int columnMovement;

	Direction(int rowMovement, int columnMovement) {
		this.rowMovement = rowMovement;
		this.columnMovement = columnMovement;
	}

	public static List<Direction> getKnightDirection() {
		return List.of(SSE, SSW, NNE, NNW, EEN, EES, WWN, WWS);
	}

	public static List<Direction> getKingDirection() {
		return List.of(E, W, S, N, NE, NW, SE, SW);
	}

	public static List<Direction> getRookDirection() {
		return List.of(E, W, S, N);
	}

	public static List<Direction> getBishopDirection() {
		return List.of(NE, NW, SE, SW);
	}

	public static List<Direction> getQueenDirection() {
		return List.of(E, W, S, N, NE, NW, SE, SW);
	}

	public static List<Direction> getPawnByTeam(Team team) {
		if (team.isBlack()) {
			return List.of(S, SE, SW);
		}
		return List.of(N, NE, NW);
	}


	public int addRow(final int row) {
		return row + rowMovement;
	}

	public int addColumn(final int column) {
		return column + columnMovement;
	}

	public boolean match(final int differenceRow, final int differenceColumn) {
		return rowMovement == differenceRow && columnMovement == differenceColumn;
	}
}
