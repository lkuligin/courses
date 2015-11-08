package inversions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

//reads a file and returns an array
public class Tsv_Read {
	private String FileName;

	public Tsv_Read(String FileName) {
		this.FileName = FileName;
	}

	public int[] ReadFile() throws Exception {
		System.out.println(FileName);
		BufferedReader TsvFile = new BufferedReader(new FileReader(FileName));
		String Row = TsvFile.readLine();
		List<Integer> res = new ArrayList<Integer>();
		
		while (Row != null) {
			res.add(Integer.parseInt(Row));
			Row = TsvFile.readLine();
		}
		
		int[] res1 = new int[res.size()];
		for(int j = 0; j < res.size(); j++) res1[j] = res.get(j);
		
		TsvFile.close();		
		
		return res1;
	}

}
