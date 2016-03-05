package logic.commands;

import logic.Logic;

public class InvalidCommand implements Command {

    private String _error_message;

    public InvalidCommand() {
    }

    public InvalidCommand(String _error_message) {
        this._error_message = _error_message;
    }

    public String get_error_message() {
        return _error_message;
    }

    public void set_error_message(String _error_message) {
        this._error_message = _error_message;
    }

    @Override
    public void execute(Logic logic) {

    }
}
