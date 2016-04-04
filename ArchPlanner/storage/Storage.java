package storage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	private String _defaultFilePath;

	//This is the name of the linker file that indicates the storage file location
	private String _linkerName;

	//This the storage file to be manipulated by the program
	private File _storageFile;

	//This is the linker file to be manipulated by the program
	private File _linkerFile;

	//This arraylist is used to store the strings of text in the file
	private ArrayList<Task> _masterList = new ArrayList<Task>();
	
	private final String ERROR_MESSAGE_CREATE_FILE_FAILED = "Failed to create file";
	private final String ERROR_MESSAGE_ACCESS_FILE_FAILED = "Failed to acesss file";

	public void loadStorageFile() {
		_defaultFilePath = "archplanner.txt";
		_filePath = "";
		_linkerName = "linker.txt";
		_linkerFile = new File(_linkerName);

		if (!_linkerFile.exists() || !_linkerFile.isFile()) {
			createFile(_linkerFile);
		}
		initializeFile();
	}

	private void initializeFile() {
		try {
			Scanner scan = new Scanner(_linkerFile);
			if (scan.hasNextLine()) {
				_filePath = scan.nextLine();
			} else {
				_filePath = _defaultFilePath;
			}
			scan.close();
		} catch (Exception e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}

		_storageFile = new File(_filePath);

		if (!_storageFile.exists() || !_storageFile.isFile()) {
			_filePath = _defaultFilePath;
			createFile(_storageFile);
		} else {
			readStorageFile();
		}
	}
	
	public void setFilePath(String filePath) {
		try {
			FileWriter fileWriter;
			_storageFile = new File(_filePath);
			_storageFile.delete();
			_filePath = filePath;
			fileWriter = new FileWriter(_linkerFile, false);
			fileWriter.append(_filePath + "\r\n");
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
		try {
			fileWriter = new FileWriter(_filePath);
			fileWriter.write(json);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println(ERROR_MESSAGE_ACCESS_FILE_FAILED);
		}
	}
	
	public ArrayList<Task> getMasterList() {
		return _masterList;
	}

	private void createFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println(ERROR_MESSAGE_CREATE_FILE_FAILED);
		}
	}

	private void readStorageFile() {
		Gson gson = new Gson();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(_filePath));
			_masterList = gson.fromJson(bufferedReader, new TypeToken<ArrayList<Task>>(){}.getType());
		} catch (Exception e) {
			_storageFile.delete();
			createFile(_storageFile);
		}
	}
}
