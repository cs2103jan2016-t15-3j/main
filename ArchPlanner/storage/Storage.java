package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import logic.Task;

/**
 * This class is used to manage the accessing and storing of storage file.
 * 
 * @@author A0140021J
 *
 */
public class Storage {

	//This list is used to store the entries of tasks in the file
	private ArrayList<Task> _masterList = new ArrayList<Task>();

	//This is the file path of the file to be manipulated by the program.
	private String _filePath;

	//This the storage file to be manipulated by the program.
	private File _storageFile;

	//This is the linker file to be manipulated by the program.
	private File _linkerFile;

	//This is the file name of the file to be manipulated by the program.
	private String _fileName = "archplanner.txt";

	//This is the name of the linker file that indicates the storage file location.
	private String _linkerName = "linker.txt";
	
	//These are the variables used to manage the file system.
	private final String SYSTEM_NAME = "os.name";
	private final String SYSTEM_DRIVE = "SystemDrive";
	private final String USER_NAME = "user.name";
	private final String SYSTEM_NAME_MAC_OS = "Mac OS";
	private final String SYSTEM_NAME_WINDOWS = "Windows";
	private final String USERS_DIRECTORY = "/Users/";
	private final String MAC_OS_PROGRAM_SUB_DIRECTORY = "/Documents/ArchPlanner/";
	private final String WINDOWS_PROGRAM_SUB_DIRECTORY = "/AppData/Local/ArchPlanner/";

	//These are the error message to be output when error occurs.
	private final String ERROR_MESSAGE_CREATE_FILE_FAILED = "Failed to create file";
	private final String ERROR_MESSAGE_ACCESS_FILE_FAILED = "Failed to acesss file";

	//This constant string variable is used to append messages for readability.
	private final String STRING_EMPTY = "";
	private final String STRING_NEW_LINE = "\r\n";

	//This is constructor of the class.
	public Storage() {
		_storageFile = null;
		_linkerFile = null;
		setFilePathToDefault();
		loadStorageFile();
	}

	/**
	 * This is setter method for master list.
	 * 
	 * @param list This will be the master list.
	 */
	public void setMasterList(ArrayList<Task> list) {
		_masterList = list;
	}

	/**
	 * This is the setter method for file path.
	 * 
	 * @param filePath This will be the file path.
	 */
	public void setFilePath(String filePath) {
		_filePath = filePath;
	}

	/**
	 * This is setter method for storage file.
	 * 
	 * @param file This will be the storage file.
	 */
	public void setStorageFile(File file) {
		_storageFile = file;
	}

	/**
	 * This is setter method for linker file.
	 * 
	 * @param file This will be the linker file.
	 */
	public void setLinkerFile(File file) {
		_linkerFile = file;
	}

	/**
	 * This is getter method for master list.
	 * 
	 * @return master list.
	 */
	public ArrayList<Task> getMasterList() {
		return _masterList;
	}

	/**
	 * This is getter method for file path.
	 * 
	 * @return file path.
	 */
	public String getFilePath() {
		return _filePath;
	}

	/**
	 * This is getter method for storage file.
	 * 
	 * @return storage file.
	 */
	public File getStorageFile() {
		return _storageFile;
	}

	/**
	 * This is getter method for linker file.
	 * 
	 * @return storage file.
	 */
	public File getLinkerFile() {
		return _linkerFile;
	}

	/**
	 * This is getter method for file name.
	 * 
	 * @return file name.
	 */
	public String getFileName() {
		return _fileName;
	}

	/**
	 * This is getter method for linker file name.
	 * 
	 * @return linker file name.
	 */
	public String getLinkerName() {
		return _linkerName;
	}

	/**
	 * This method is used to load the files.
	 */
	public void loadStorageFile() {
		setLinkerFile(new File(getLinkerPath()));
		createLinkerFileIfNotExists();
		initializeFile();
	}
	
	/**
	 * This method is used to set new file path.
	 * 
	 * @param filePath This will be the new file path.
	 */
	public void setNewFilePath(String filePath) {
		try {
			FileWriter fileWriter;
			setFilePath(filePath);
			setStorageFile(new File(getFilePath()));
			createProgramFolder();
			fileWriter = new FileWriter(getLinkerPath(), false);
			fileWriter.append(getFilePath() + STRING_NEW_LINE);
			fileWriter.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
	}

	/**
	 * This method is used to set file path to an existing file path.
	 * 
	 * @param filePath This is the exisitng file path.
	 */
	public void setExistingFilePath(String filePath) {
		try {
			FileWriter fileWriter;
			setFilePath(filePath);
			setStorageFile(new File(getFilePath()));
			createProgramFolder();
			fileWriter = new FileWriter(getLinkerPath(), false);
			fileWriter.append(getFilePath() + STRING_NEW_LINE);
			fileWriter.close();
			readStorageFile();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
	}

	/**
	 * This method is used to set file path to default.
	 */
	public void setDefaultFilePath() {
		try {
			FileWriter fileWriter;
			setFilePath(getDefaultFilePath());
			setStorageFile(new File(getDefaultFilePath()));
			createProgramFolder();
			fileWriter = new FileWriter(getLinkerPath(), false);
			fileWriter.append(getFilePath() + STRING_EMPTY);
			fileWriter.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
	}

	/**
	 * This method is used to write the list of tasks to the file.
	 * 
	 * @param list This is the lists of tasks to be saved to the file.
	 */
	public void writeStorageFile(ArrayList<Task> list) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type type = new TypeToken<ArrayList<Task>>() {}.getType();
		String json = gson.toJson(list, type);

		FileWriter fileWriter;
		createLinkerFileIfNotExists();

		try {
			fileWriter = new FileWriter(getFilePath());
			fileWriter.write(json);
			fileWriter.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
	}

	/**
	 * This method is used to create linker file if it does not exists.
	 */
	private void createLinkerFileIfNotExists() {
		if (!getLinkerFile().exists() || !getLinkerFile().isFile()) {
			createProgramFolder();
			createFile(_linkerFile);
		}
	}

	/**
	 * This method is used to create folder of this program.
	 */
	private void createProgramFolder() {
		String programFolderDirectory = getDefaultDirectory().substring(0, getDefaultDirectory().length() - 1);
		new File(programFolderDirectory).mkdirs();
	}

	/**
	 * This method is used to load the file using the file path stored in linker file. If file path is not found,
	 * file path is set to default.
	 */
	private void initializeFile() {
		try {
			Scanner scan = new Scanner(getLinkerFile());
			if (scan.hasNextLine()) {
				setFilePath(scan.nextLine());
				scan.close();
			} else {
				setFilePathToDefault();
			}
			scan.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
		setStorageFile(new File(getFilePath()));
		readStorageFile();
	}

	/**
	 * This method is used to read storage file.
	 */
	private void readStorageFile() {
		Gson gson = new Gson();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(getFilePath()));
			setMasterList(gson.fromJson(bufferedReader, new TypeToken<ArrayList<Task>>(){}.getType()));
			bufferedReader.close();
		} catch (Exception e) {
			getStorageFile().delete();
			createFile(getStorageFile());
		}
	}

	/**
	 * this method is used to create new file.
	 * 
	 * @param file This is the file to be created.
	 */
	private void createFile(File file) {
		try {
			file.createNewFile();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_CREATE_FILE_FAILED);
		}
	}

	/**
	 * This method is used to retrieve the default directory for storing of file.
	 * 
	 * @return default directory.
	 */
	private String getDefaultDirectory() {
		String systemName = System.getProperty(SYSTEM_NAME);
		String systemDrive = System.getenv(SYSTEM_DRIVE);
		String userName = System.getProperty(USER_NAME);
		String defaultDirectory;
		if (systemName.startsWith(SYSTEM_NAME_MAC_OS)) {
			defaultDirectory = USERS_DIRECTORY + userName + MAC_OS_PROGRAM_SUB_DIRECTORY;
		} else if (systemName.startsWith(SYSTEM_NAME_WINDOWS)) {
			defaultDirectory = systemDrive + USERS_DIRECTORY + userName + WINDOWS_PROGRAM_SUB_DIRECTORY;
		} else {
			defaultDirectory = STRING_EMPTY;
		}
		return defaultDirectory;
	}
	/**
	 * This method is used to get the file path of the linker file.
	 * 
	 * @return linker file's file path.
	 */
	private String getLinkerPath() {
		String defaultLinkerPath = getDefaultDirectory() + getLinkerName();
		return defaultLinkerPath;
	}

	/**
	 * This method is used to get the file path of the storage file.
	 * 
	 * @return storage file's file path.
	 */
	private String getDefaultFilePath() {
		String defaultLinkerPath = getDefaultDirectory() + getFileName();
		return defaultLinkerPath;
	}

	/**
	 * This method is used to set file path to default.
	 */
	private void setFilePathToDefault() {
		setFilePath(getDefaultDirectory() + getFileName());
	}
}
