import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class Check {
	public static void main(String[] args) throws FileNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\gsharma\\Downloads\\file.json"));	
				
		Gson gson = new Gson();
		
		Product[] myData = gson.fromJson(br, Product[].class);
		List<String> set = new ArrayList<>();
		
		for (Product product : myData) {
			//System.out.println(product.name + ", " + product.price + ", " + product.category.name);
			
			if(!set.contains(product.category.name)) {
				set.add(product.category.name);	
			}
		}
		int i = 0;
		for (String string : set) {
			System.out.println(++i + ": " + string);
		}
		
	}
}
