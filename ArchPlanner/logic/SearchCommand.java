package logic;

public class SearchCommand implements Command {
	
	private String _partialDescription;

	public void setPartialDescription(String partialDescription) {
		_partialDescription = partialDescription;
	}

	public String getPartialDescription() {
		return _partialDescription;
	}
}
