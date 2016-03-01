import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class specifies the process of accessing, updating and saving of data to a file.
 * @author riyang
 *
 */
public class Storage {

	//This is the name of the file to be manipulated
	public String _fileName;

	//This the File to be manipulated by the program
	public File _file;

	//This arraylist is used to store the strings of text in the file
	public ArrayList<Task> _tasksList = new ArrayList<Task>();
	
	public void setTasksList(ArrayList<Task> tasksList) {
		_tasksList = tasksList;
	}
	
	public ArrayList<Task> getTasksList() {
		return _tasksList;
	}

	public void loadStorageFile() throws IOException, ClassNotFoundException {
		_fileName = "Storage.srl";
		_file = new File(_fileName);

		if (!_file.exists() || !_file.isFile()) {
			createStorageFile();
		} else {
			readStorageFile();
		}
	}

	public void createStorageFile() throws IOException {
		_file.createNewFile();
	}
	
	public void readStorageFile() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream(_fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			_tasksList = (ArrayList<Task>) ois.readObject();
			ois.close();
		} catch (EOFException e) {
			System.out.println("End Of File!");
		}
	}
	
	public void writeStorageFile(ArrayList<Task> tasksList) throws IOException {
		setTasksList(tasksList);
		FileOutputStream fos = new FileOutputStream(_fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(_tasksList);
		oos.close();
	}
}
