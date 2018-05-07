package report;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class GroceryStore extends Report{

	@Override
	protected List<Row> prepareData() {
		List<Row> data = new ArrayList<Row>();
		data.add(new TestRow("apple", "fruit",20.5f, 2));
		data.add(new TestRow("banana", "fruit",10.5f, 5));
		data.add(new TestRow("potato", "vegitable",15.2f, 5));
		return data;	
	}

	@Override
	protected Response prepareEmptyResponse(Filter f) {
		TestFilter filter = (TestFilter) f;
		TestResponse response = new TestResponse();
		response.name= filter.name;
		response.type = filter.type;
		return response;
	}
	
	public Filter getFilter(String name, String type) {
		return new TestFilter(name, type);
	}

}
class TestRow implements Row{
	String name;
	String type;
	float price;
	int quantity;
	
	@Override
	public String prepareDimensionsKey() {
		return name + "-" + type;
	}

	public TestRow(String dimension1, String dimension2, float metric1, int metric2) {
		super();
		this.name = dimension1;
		this.type = dimension2;
		this.price = metric1;
		this.quantity = metric2;
	}
	
}
class TestFilter implements Filter{
	String name;
	String type;
	@Override
	public boolean filterIn(Row row) {
		TestRow r = (TestRow) row;
		if( 
				(name == null || name == r.name)
				&&(type == null || type == r.type)
				)
			return true;
		return false;
	}
	public TestFilter(String dimension1, String dimension2) {
		super();
		this.name = dimension1;
		this.type = dimension2;
	}	
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class TestResponse implements Response{
	// marking public just for test. do write getters.
	public String name; 
	public String type;
	public float price;
	public int quantity;
	public float averagePricePerItem;
	@Override
	public void addMetrics(Row row) {
		TestRow r = (TestRow) row;
		price += r.price;
		quantity += r.quantity;
	}
	@Override
	public void setFilterValues(Filter f) {
		TestFilter filter = (TestFilter) f;
		name = filter.name;
		type = filter.type;
	}
	@Override
	public void setDerivedMetrics() {
		averagePricePerItem = quantity==0 ? 0 :price/quantity;		
	}
	
}
