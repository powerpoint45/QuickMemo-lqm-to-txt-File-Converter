import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("deprecation")
public class Main {

	

	public static void main(String [] args) {
		if (args.length>0) {
			for (String f : args) {
				processFile(new File(f));
			}
		}else {
			File curDir = new File(".");
	        ArrayList<File> files = getAllFiles(curDir);
	        for (File f: files) {
	        	processFile(f);
	        }
		}
	}
	
	
	private static ArrayList<File> getAllFiles(File curDir) {
        File[] filesList = curDir.listFiles();
        ArrayList<File> lqmFiles = new ArrayList<>();
        for(File f : filesList){
            if(f.isFile() && f.getName().endsWith(".lqm")){
                lqmFiles.add(f);
            }
        }
        
        return lqmFiles;

    }
	
	public static void writeTextFile(File f, Object o){
		try {
			File textFile = new File(f.getName().replace(".lqm", ".txt"));
			PrintWriter out = new PrintWriter(textFile);
			out.print(o);
			out.close();
			System.out.println(f.getName()+" ---> "+textFile.getName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void processFile(File f) {
		if (f.exists()) {
			ZipFile zf = null;
			try {
				zf = new ZipFile(f);
				InputStream is = zf.getInputStream(zf.getEntry("memoinfo.jlqm"));
				JSONObject jsonObject = null;
				JSONParser jsonParser = new JSONParser();
				try {
					jsonObject = (JSONObject)jsonParser.parse(
					      new InputStreamReader(is, "UTF-8"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				JSONArray a = (JSONArray)jsonObject.get("MemoObjectList");
				JSONObject o = (JSONObject)a.get(0);
				writeTextFile(f, o.get("DescRaw"));				
				
			} catch (ZipException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (zf!=null)
					try {
						zf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}else
			System.out.println(f.getName() +" Does not exist");
	}
}
