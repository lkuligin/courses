package tests;

import static org.junit.Assert.*;
import examples.checkSum;

import org.junit.Test;

public class checkSumTest {

	@Test
	public final void testCount2sums() {
		checkSum cs = new checkSum();
		cs.addElements(new int[] {2,4,8,16,32,64,128,256,512,1024});
		int res = cs.count2sums(-10000, 10000);
		assertEquals("test1 2sum", 45, res);
	}

	@Test
	public final void test1Count2sums() {
		checkSum cs = new checkSum();
		cs.addElements(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, -10});
		int res = cs.count2sums(-10000, 10000);
		assertEquals("2sum test avoid duplication", 3, res);
	}

}
