package logic.commands;

import logic.Logic;

public class SearchCommand implements Command {
	
	private String _partialDescription;

	public SearchCommand(String _partialDescription) {
		this._partialDescription = _partialDescription;
	}

	public void setPartialDescription(String partialDescription) {
		_partialDescription = partialDescription;
	}

	public String getPartialDescription() {
		return _partialDescription;
	}

	@Override
	public void execute(Logic logic) {

	}
}
