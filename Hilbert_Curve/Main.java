package edu.bu;

/**
 * @author lei
 * This class is for test the result
 * @ZvalueConvert
 * 	1. PointToZValue(int bits, int x, int y), translate point to zvalue
 * 	2. ZValueToPoint(int bits, int zValue), translate zvalue to point
 *
 * @HilbertConvert
 * 	1. HilbertEncode(int bits, int x, int y), translate point to hilbert value
 * 	2. hilbertValueToPoint(int bits, int hilbertValue), translate hilbert value to point
 * 
 */
public class Main {
	public static void main(String[] args) {
		System.out.println("Zvalue Test cases:");
		System.out.println("Point to Zvalue");
		ZvalueConvert zc = new ZvalueConvert();
		zc.PointToZValue(2, 0, 0);
		zc.PointToZValue(3, 0, 100);
		System.out.println();
		System.out.println("Zvalue to Point");
		zc.ZValueToPoint(5, 0);
		zc.ZValueToPoint(5, 15);
		System.out.println();

		System.out.println("Hilbert Test cases:");
		HilbertConvert hc = new HilbertConvert();
		System.out.println("Hlibert value to point");
		hc.hilbertValueToPoint(8, 3000);
		hc.hilbertValueToPoint(2, 3);
		System.out.println();

		System.out.println("Point to Hilbert value");
		hc.HilbertEncode(0, 0, 2);
		hc.HilbertEncode(20, 55, 2);
		hc.HilbertEncode(47, 62, 6);
	}
}
