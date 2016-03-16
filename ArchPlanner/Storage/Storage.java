package storage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import logic.Task;

/**
 * This class specifies the process of accessing, updating and saving of data to a file.
 * @author riyang
 *
 */
public class Storage {

	//This is the name of the file to be manipulated
	private String _fileName = "Storage.txt";

	//This the File to be manipulated by the program
	private File _file;

	//This arraylist is used to store the strings of text in the file
	private  ArrayList<Task> _masterList = new ArrayList<Task>();

	public  void setMasterList(ArrayList<Task> list) {
		_masterList = list;
	}

	public  ArrayList<Task> getMasterList() {
		return _masterList;
	}

	public  void loadStorageFile() {
		_file = new File(_fileName);

		if (!_file.exists() || !_file.isFile()) {
			createStorageFile();
		} else {
			try {
				readStorageFile();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public  void createStorageFile() {
		try {
			_file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readStorageFile() throws IOException, ClassNotFoundException {
		Gson gson = new Gson();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(_fileName));
		_masterList = gson.fromJson(bufferedReader, new TypeToken<ArrayList<Task>>(){}.getType());
	}

	public void writeStorageFile(ArrayList<Task> list) {
		Gson gson = new Gson();
	    Type type = new TypeToken<ArrayList<Task>>() {}.getType();
	    String json = gson.toJson(list, type);
	    
	    try {
	    	FileWriter fileWriter = new FileWriter(_fileName);
	    	fileWriter.write(json);
	    	fileWriter.close();
	    } catch(IOException e) {
	    	
	    }
	}
}
