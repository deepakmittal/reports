package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class MonthTest {
	@Test
	public void test() {
		assertEquals("January", Month.toProperMonthString("1"));
		assertEquals("January", Month.toProperMonthString("jan"));
		assertEquals("January", Month.toProperMonthString("Jan"));
		assertEquals("January", Month.toProperMonthString("January"));
		assertNull(Month.toProperMonthString("NoMonth"));
		assertNull(Month.toProperMonthString(null));
	}

}
