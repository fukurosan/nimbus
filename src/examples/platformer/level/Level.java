package examples.platformer.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level {
	
	public static int[] readMapTo1DArray(String filePath) {
		String dataString = "";
		String row = "";
		try {
			InputStream inputStream = Level.class.getResourceAsStream(filePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((row = bufferedReader.readLine()) != null) {
				dataString += row + ",";
			}
			bufferedReader.close();
			int[] dataArray = Arrays.stream(dataString.split(",")).mapToInt(Integer::parseInt).toArray();
			return dataArray;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int[][] readMapTo2DArray(String filePath) {
		try {
			InputStream inputStream = Level.class.getResourceAsStream(filePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			List<int[]> rows = new ArrayList<int[]>();
			String row = "";
			while ((row = bufferedReader.readLine()) != null) {
				rows.add(Arrays.stream(row.split(",")).mapToInt(Integer::parseInt).toArray());
			}
			bufferedReader.close();
			int[][] result = new int[rows.size()][];
			for(int i = 0; i < rows.size(); i++) {
				result[i] = rows.get(i);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
