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

	private String INVALID_DIRECTORY = "File Directory does not exist. Please set a different file path.";
	private String INVALID_FILE_NAME = "A file with identical file name is found. Please set a different file name.";
	private String INVALID_FILE_PATH = "Invalid file path";

	public SetCommand(String filePath) {
		_filePath = filePath;
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
		File fileDirectory = null;
		try {
			file = new File(_filePath);
			fileDirectory = new File(file.getParent());
			if (!file.exists() || !file.isFile()) {
				setFilePathIfDirectoryExists(storage, fileDirectory);
			} else {
				_invalidCommand = new InvalidCommand(INVALID_FILE_NAME);
			}
		} catch (Exception e) {
			_invalidCommand = new InvalidCommand(INVALID_FILE_PATH);
		}

	}

	private void setFilePathIfDirectoryExists(Storage storage, File fileDirectory) {
		if (fileDirectory.exists() && fileDirectory.isDirectory()) {
			storage.setFilePath(_filePath);
		} else {
			_invalidCommand = new InvalidCommand(INVALID_DIRECTORY);
		}
	}

	public Command execute(ListsManager listsManager, HistoryManager historyManager) {
		return null;
	}
}
