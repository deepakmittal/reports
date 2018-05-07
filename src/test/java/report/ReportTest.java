package report;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReportTest {
	@Test
	public void test() {
		GroceryStore report = new GroceryStore();
		report.init();
		TestResponse r = (TestResponse) report.fetchData(report.getFilter("apple", null));
		assertEquals(r.quantity, 2);
		assertEquals(r.name, "apple");
		
		r = (TestResponse) report.fetchData(report.getFilter(null, "fruit"));
		assertEquals(r.quantity, 2 + 5);
		System.out.println("price:"+r.price + " vs " + (10.5f + 20.5f) );
		assertEquals(r.price, 10.5f + 20.5f,0.0001);
		System.out.println("avg price:"+r.averagePricePerItem + " vs " + ((10.5f + 20.5f)/(2+5)) );
		assertEquals(r.averagePricePerItem, (10.5f + 20.5f)/(2+5),0.0001);
	}
}
