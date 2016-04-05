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
 * This class specifies the process of accessing, updating and saving of data to a file.
 * @author riyang
 *
 */
public class Storage {

	//This is the file path of the file to be manipulated by the program
	private String _filePath;

	//This the storage file to be manipulated by the program
	private File _storageFile;

	//This is the linker file to be manipulated by the program
	private File _linkerFile;

	//This is the file name of the file to be manipulated by the program
	private final String _fileName = "archplanner.txt";

	//This is the name of the linker file that indicates the storage file location
	private final String _linkerName = "linker.txt";

	//These are the error message to be output when error occurs
	private final String ERROR_MESSAGE_CREATE_FILE_FAILED = "Failed to create file";
	private final String ERROR_MESSAGE_ACCESS_FILE_FAILED = "Failed to acesss file";

	//This arraylist is used to store the strings of text in the file
	private ArrayList<Task> _masterList = new ArrayList<Task>();

	public Storage() {
		_storageFile = null;
		_linkerFile = null;
		//setDefaultFilePath();
	}

	public void loadStorageFile() {
		_linkerFile = new File(getLinkerPath());
		createLinkerFileIfNotExists();
		initializeFile();
	}

	private void createLinkerFileIfNotExists() {
		if (!_linkerFile.exists() || !_linkerFile.isFile()) {
			createProgramFolder();
			createFile(_linkerFile);
		}
	}

	private void createProgramFolder() {
		String programFolderDirectory = getDefaultDirectory().substring(0, getDefaultDirectory().length() - 1);
		new File(programFolderDirectory).mkdirs();
	}

	private void initializeFile() {
		try {
			Scanner scan = new Scanner(_linkerFile);
			if (scan.hasNextLine()) {
				_filePath = scan.nextLine();
				scan.close();
			} else {
				setDefaultFilePath();
			}
			scan.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
		_storageFile = new File(getFilePath());
		readStorageFile();
	}

	public void setFilePath(String filePath) {
		try {
			FileWriter fileWriter;
			_storageFile.delete();
			_filePath = filePath;
			_storageFile = new File(getFilePath());
			createProgramFolder();
			fileWriter = new FileWriter(getLinkerPath(), false);
			fileWriter.append(getFilePath() + "\r\n");
			fileWriter.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
	}

	public void writeStorageFile(ArrayList<Task> list) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type type = new TypeToken<ArrayList<Task>>() {}.getType();
		String json = gson.toJson(list, type);

		FileWriter fileWriter;
		createLinkerFileIfNotExists();
		
		try {
			fileWriter = new FileWriter(_filePath);
			fileWriter.write(json);
			fileWriter.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
	}

	public ArrayList<Task> getMasterList() {
		return _masterList;
	}

	public String getFilePath() {
		return _filePath;
	}

	public String getFileName() {
		return _fileName;
	}

	public String getLinkerName() {
		return _linkerName;
	}

	private void readStorageFile() {
		Gson gson = new Gson();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(_filePath));
			_masterList = gson.fromJson(bufferedReader, new TypeToken<ArrayList<Task>>(){}.getType());
			bufferedReader.close();
		} catch (Exception e) {
			_storageFile.delete();
			createFile(_storageFile);
		}
	}

	private void createFile(File file) {
		try {
			file.createNewFile();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_CREATE_FILE_FAILED);
		}
	}

	private String getDefaultDirectory() {
		String systemDrive = System.getenv("SystemDrive");
		String userName = System.getProperty("user.name");
		String defaultDirectory = systemDrive + "/Users/" + userName + "/AppData/Local/ArchPlanner/";
		return defaultDirectory;
	}

	private String getLinkerPath() {
		String defaultLinkerPath = getDefaultDirectory() + getLinkerName();
		return defaultLinkerPath;
	}

	private void setDefaultFilePath() {
		_filePath = getDefaultDirectory() + getFileName();
	}
}
