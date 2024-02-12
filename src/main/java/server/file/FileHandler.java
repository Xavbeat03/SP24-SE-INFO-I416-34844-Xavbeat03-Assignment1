package server.file;


import java.io.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FileHandler {

	private static final String FILE_PATH = "./data/map.txt";
	private FileHandler(){}

	public static synchronized void storeValue(String value, String key){
		sleepForRandomShortDuration();
		if(getValue(key).length == 0) {
			writeFile("%s,%s%n".formatted(key, value));
		} else {
			try(BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()))){
				String line = fileReader.readLine();
				StringBuilder totalFile = new StringBuilder();
				while(line!=null){
					if(line.split(",")[0].equals(key)){
						totalFile.append("%s,%s%n".formatted(key,value));
					} else {
						totalFile.append(line);
						line = fileReader.readLine();
					}
				}
				writeFile(String.valueOf(totalFile));
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private static synchronized void writeFile(String totalFile) {
		try (FileWriter fileWriter = new FileWriter(getFilePath())) {
			fileWriter.write(totalFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized String[] getValue(String key){
		sleepForRandomShortDuration();
		try(BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()))){
			String line = fileReader.readLine();
			while(line!=null){
				if(line.split(",")[0].equals(key))return line.split(",");
				line = fileReader.readLine();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return new String[]{};
	}
	private static String getFilePath(){return FILE_PATH;}

	private static synchronized void sleepForRandomShortDuration(){
		try {
			Random random = new Random();
			TimeUnit.MILLISECONDS.sleep(
				random.nextInt(1000)
			);
		} catch (InterruptedException i){
			i.printStackTrace();
		}
	}
}
