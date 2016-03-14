package logic;
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
	private final String _fileName = "Storage.srl";

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
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
		try {
			FileInputStream fis = new FileInputStream(_fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			_masterList = (ArrayList<Task>) ois.readObject();
			ois.close();
		} catch (EOFException e) {
			System.out.println("End Of File!");
		}
	}
	
	public void writeStorageFile(ArrayList<Task> list) {
		setMasterList(list);
		FileOutputStream fos;
        try {
            fos = new FileOutputStream(_fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(_masterList);
            oos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
}
