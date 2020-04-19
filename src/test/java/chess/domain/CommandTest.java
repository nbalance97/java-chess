package chess.domain;

import chess.command.Command;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CommandTest {

    @ParameterizedTest
    @ValueSource(strings = {"start", "end", "move", "status"})
    void Command_StringCommand_returnInstance(String command) {
        Assertions.assertThat(Command.from(command)).isInstanceOf(Command.class);
    }

    @Test
    void getCommand_ValidCommand_returnCommand() {
        Command command = Command.from("move a1 a2");

        assertThat(command.getCommand()).isEqualTo("move");
    }
}