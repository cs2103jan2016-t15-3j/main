package logic.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import logic.HistoryManager;
import logic.ListsManager;
import logic.Logic;
import logic.Task;
import storage.Storage;

/**
 * This class is used to execute set command.
 * 
 * @@author A0140021J
 *
 */
public class SetCommand implements CommandInterface {

	//This is the logger used to log and observe the changes when program runs.
	static Logger log = Logger.getLogger(Logic.class.getName());

	//This is the file path of the storage file.
	private String _filePath;

	//This is the invalidCommand to be initialized if set command is invalid.
	private InvalidCommand _invalidCommand;

	//This is the message of an SetCommand object to be displayed.
	private String _message;

	//These constant string variables are the standard format for display message when  set command is executed successfully.
	private final String MESSAGE_SET_NEW_FILE_PATH = "created \"%1$s\"";
	private final String MESSAGE_SET_EXISTING_FILE_PATH = "set filepath to \"%1$s\"";
	private final String MESSAGE_SET_DEFAULT_FILE_PATH= "The filepath has been set to default";

	//These are constant string variables of error messages to be displayed if set command is invalid.
	private final String INVALID_DIRECTORY = "File Directory does not exist.";
	private final String INVALID_FILE_PATH = "Invalid file path";
	private final String INVALID_FILE = "Invalid file. Please confirm.";

	//This constant string variable is file extension of the storage file.
	private final String FILE_EXTENSION = ".txt";

	//These are constant string variables for logging.
	private final String  LOGGER_MESSAGE_EXECUTING_SET_COMMAND = "Executing set command...";
	private final String  LOGGER_MESSAGE_COMPLETED_SET_COMMAND = "Completed set command.";

	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";

	//This is constructor of the class.
	public SetCommand(String filePath) {
		setFilePath(filePath);
		_message = STRING_EMPTY;
	}

	public void setFilePath(String filePath) {
		if (filePath != null) {
			_filePath = filePath + FILE_EXTENSION;
		}
	}

	/**
	 * This is setter method for SetCommand's invalidCommand
	 * 
	 * @param invalidCommand	This will be the invalidCommand of the SetCommand.
	 */
	public void setInvalidCommand(InvalidCommand invalidCommand) {
		_invalidCommand = invalidCommand;
	}

	/**
	 * This is setter method for SetCommand's message.
	 * 
	 * @param message	This will be the message of the SetCommand.
	 */
	public void setMessage(String message) {
		_message = message;
	}

	/**
	 * This is getter method for SetCommand's filePath.
	 * 
	 * @return	file path.
	 */
	public String getFilePath() {
		return _filePath;
	}

	/**
	 * This is getter method for SetCommand's invalidCommand.
	 * 
	 * @return	invalidCommand.
	 */
	public InvalidCommand getInvalidCommand() {
		return _invalidCommand;
	}

	/**
	 * This method is getter method SetCommand's message.
	 * 
	 * @return	message.
	 */
	public String getMessage() {
		return _message;
	}

	/**
	 * This method will not be called.
	 */
	public CommandInterface execute() {
		return null;
	}

	/**
	 * This method will not be called.
	 */
	public CommandInterface execute(ListsManager listsManager, HistoryManager historyManager) {
		return null;
	}

	/**
	 * This method is used to execute set command.
	 */
	public CommandInterface execute(ListsManager listsManager, Storage storage) {
		log.info(LOGGER_MESSAGE_EXECUTING_SET_COMMAND);
		clearIndexList(listsManager);
		setFilePath(listsManager, storage);
		if (getInvalidCommand() != null) {
			log.info(getInvalidCommand().getMessage());
			return getInvalidCommand();
		}
		log.info(getMessage());
		log.info(LOGGER_MESSAGE_COMPLETED_SET_COMMAND);
		return null;
	}

	/**
	 * This method is used to clear indexList in ListsManager.
	 * 
	 * @param listsManager	This is the ListsManager.
	 */
	private void clearIndexList(ListsManager listsManager) {
		listsManager.getIndexList().clear();
	}

	/**
	 * This method is used to set file path.
	 * 
	 * @param listsManager	This is the ListsManager.
	 * @param storage		This is the Storage.
	 */
	private void setFilePath(ListsManager listsManager, Storage storage) {
		if (_filePath == null) {
			setDefaultFilePath(listsManager, storage);
			return;
		}

		try {
			File file = null;
			file = new File(_filePath);
			if (!file.exists()) {
				setNewFilePathIfDirectoryExists(storage, file.getParent());
				setMessage(String.format(MESSAGE_SET_NEW_FILE_PATH, file.getCanonicalPath()));
			} else if (file.isFile()) {
				setFilePathToExistingFilePath(listsManager, storage, file);
			}
		} catch (Exception e) {
			_invalidCommand = new InvalidCommand(INVALID_FILE_PATH);
		}

	}

	/**
	 * This method is used to file path to the default file path.
	 * 
	 * @param listsManager	This is the ListsManager.
	 * @param storage		This is the Storage.
	 */
	private void setDefaultFilePath(ListsManager listsManager, Storage storage) {
		storage.setDefaultFilePath();
		listsManager.setUpLists(storage.getMasterList());
		setMessage(MESSAGE_SET_DEFAULT_FILE_PATH);
	}

	/**
	 * This method is used to set file path to an existing file path.
	 * 
	 * @param listsManager	This is the ListsManager.
	 * @param storage		This is the Storage.
	 * @param file			This is the file of the filePath.
	 */
	private void setFilePathToExistingFilePath(ListsManager listsManager, Storage storage, File file) {
		Gson gson = new Gson();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(_filePath));
			gson.fromJson(bufferedReader, new TypeToken<ArrayList<Task>>(){}.getType());
			bufferedReader.close();
			storage.setExistingFilePath(_filePath);
			listsManager.setUpLists(storage.getMasterList());
			setMessage(String.format(MESSAGE_SET_EXISTING_FILE_PATH, file.getCanonicalPath()));
		} catch (Exception e) {
			_invalidCommand = new InvalidCommand(INVALID_FILE);
		}
	}

	/**
	 * This method is used to set new file path if directory exists.
	 * 
	 * @param storage	This is the Storage.
	 * @param directory	This is the parent directory of the file path.
	 */
	private void setNewFilePathIfDirectoryExists(Storage storage, String directory) {
		File filePathDirectory = new File(directory);
		if (filePathDirectory.exists() && filePathDirectory.isDirectory()) {
			storage.setNewFilePath(_filePath);
		} else {
			_invalidCommand = new InvalidCommand(INVALID_DIRECTORY);
		}
	}
}
