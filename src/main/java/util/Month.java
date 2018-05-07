package util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Month {
	int month;
	public static final Map<Integer,String>toStringMap;
	public static final Map<String, Integer> monthMap;
	static{	
		toStringMap = getToStringMap();
		monthMap = get3letterMap();
	}
	
	public static String toProperMonthString(String month) {
		Month m = new Month(month) ;
		return m.toString();
	}
	
	private static Map<Integer,String> getToStringMap(){
		Map<Integer,String> m = new HashMap<Integer,String>();
		m.put(1, "January");
		m.put(2, "February");
		m.put(3, "March");
		m.put(4, "April");
		m.put(5, "May");
		m.put(6, "June");
		m.put(7, "July");
		m.put(8, "August");
		m.put(9, "September");
		m.put(10, "October");
		m.put(11, "November");
		m.put(12, "December");
		return Collections.unmodifiableMap(m);
	}
	
	private static Map<String,Integer> get3letterMap(){
		Map<String,Integer> m = new HashMap<String,Integer>();
		m.put("jan",1);
		m.put("feb",2);
		m.put("mar",3);
		m.put("apr",4);
		m.put("may",5);
		m.put("jun",6);
		m.put("jul",7);
		m.put("aug",8);
		m.put("sep",9);
		m.put("oct",10);
		m.put("nov",11);
		m.put("dec",12);
		return Collections.unmodifiableMap(m);
	}
	
	public String toString() {
		if(month == 0)
			return null;
		return toStringMap.get(month);
	}
	public Month(String inpMonth) {
		if (inpMonth==null)
			return;
		try {
			int m = Integer.parseInt(inpMonth);
			if (m >=1 && m<=12)
				this.month = m;
		}
		catch (NumberFormatException e) {
			String inp = inpMonth.substring(0, 3).toLowerCase();
			if(monthMap.containsKey(inp)) {
				this.month = monthMap.get(inp);
			}
		}
	}
}
