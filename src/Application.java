import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class Application {
	
	static List<Product> productList;
	
	public static void main(String[] args) throws IOException {
		
		Application.productList = new ArrayList<Product>();
		
				
		Application.kickOff();
		
		Gson gson = new Gson();
		String json = gson.toJson(Application.productList);
		
		FileWriter writer = new FileWriter("C:\\Users\\gsharma\\Downloads\\file.json");
		writer.write(json);
		writer.close();
	}

	private static void kickOff() throws IOException {
		Document doc = Jsoup.connect("http://www.epiersons.com/").get();

		Elements categories = doc
				.select("ul.sidenav_sub_indices li a.nav-side");
		
		for (Element element : categories) {
			
			System.out.println("Category: " + element.text());			
			Application.getDetailsByCategory(element.attr("abs:href"), element.text());

		}
	}

	private static void getDetailsByCategory(String baseUrl, String category) throws IOException {
		
		List<String> pagedUrlList = new ArrayList<>();
		
		pagedUrlList.add(baseUrl);
		
		Document pageOne = Jsoup.connect(baseUrl).get();
		Elements paging = pageOne.select("td.innerpage_bg_cell table tbody tr td table tbody tr td a[href^=/category/]");
		
		for(int i = 0; i < paging.size(); i++) {
			if(paging.get(i).text().equalsIgnoreCase("Next")) {
				break;
			}
			pagedUrlList.add(paging.get(i).attr("abs:href"));
		}
		
		
		for (String url : pagedUrlList) {
			
			Document doc = Jsoup.connect(url).get();

			Elements images = doc
					.select("td.innerpage_bg_cell table tbody tr td table tbody tr td a img[src$=key2=404_thumb]");
			Elements names = doc.select("div.prodtitle");
			Elements skus = doc.select("span.smallgreytext");
			Elements prices = doc.select("span.smalltext");
			

			if (names.size() == 0) {
				names = doc.select("div.promo_name");
			}
			
			if (skus.size() == 0) {
				skus = doc.select("span.prod-code");
			}
			
			if (prices.size() == 0) {
				prices = doc.select("span.price");
			}
			
			for (int i = 0; i < images.size(); i++) {
				
				Product product = new Product();
				
				product.name = names.get(i).text();
				
				product.sku = skus.get(i).text();
				product.price = prices.get(i).text();
				product.category = new Category(category);
				
				product.thumbnailImageUrl = images.get(i).attr("src");
				
				Document detailsDocument = Jsoup.connect(images.get(i).parent().attr("abs:href")).get();
				
				product.description = detailsDocument.select("#prodDetailDesc").text().toString();
				product.largeImageUrl = detailsDocument.select("#prodDetailImg").attr("src");
				
				Application.productList.add(product);
				
			}
		}
	}
}
