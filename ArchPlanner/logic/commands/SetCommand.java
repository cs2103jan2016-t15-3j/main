package logic.commands;

import java.io.File;

import logic.HistoryManager;
import logic.ListsManager;
import storage.Storage;

/**
 * Created by lifengshuang on 4/1/16.
 */
public class SetCommand implements Command {

	private String _filePath;
	private InvalidCommand _invalidCommand;

	private final String INVALID_DIRECTORY = "File Directory does not exist. Please set a different file path.";
	private final String INVALID_FILE_NAME = "A file with identical file name is found. Please set a different file name.";
	private final String INVALID_FILE_PATH = "Invalid file path";
	
	private final String FILE_EXTENSION = ".txt";
	
	public SetCommand(String filePath) {
		_filePath = filePath + FILE_EXTENSION;
	}

	public Command execute() {
		return null;
	}

	public Command execute(Storage storage) {
		setFilePath(storage);
		if (_invalidCommand != null) {
			return _invalidCommand;
		}
		return null;
	}

	private void setFilePath(Storage storage) {

		File file = null;
		try {
			file = new File(_filePath);
			if (!file.exists() || !file.isFile()) {
				setFilePathIfDirectoryExists(storage, file.getParent());
			} else {
				_invalidCommand = new InvalidCommand(INVALID_FILE_NAME);
			}
		} catch (Exception e) {
			_invalidCommand = new InvalidCommand(INVALID_FILE_PATH);
		}

	}

	private void setFilePathIfDirectoryExists(Storage storage, String directory) {
		File filePathDirectory = new File(directory);
		if (filePathDirectory.exists() && filePathDirectory.isDirectory()) {
			storage.setFilePath(_filePath);
		} else {
			_invalidCommand = new InvalidCommand(INVALID_DIRECTORY);
		}
	}

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		return null;
	}
}
