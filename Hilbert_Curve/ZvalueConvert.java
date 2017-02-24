package edu.bu;

/**
 * @author lei
 * This class contains the method to convert zvalue to point and point to zvalue
 * The implementation is based on the idea expressed in lecture by professor
 * 	calculate the binary point
 * 	shuffle binary point
 * 	convert back
 *
 */
public class ZvalueConvert {
/**
 * Print the Zvalue of corresponding point
 * @param bits
 * 				depth of zValue
 * @param x, y
 * 				coordinate of x and y
 * 	
 */
	public void PointToZValue(int bits, double x, double y) {
		double p = (int) bits;
		int adapt = (int) Math.pow(2.0, p);

		int xb = (int) ((x / 100) * (adapt - 1));
		int yb = (int) ((y / 100) * (adapt - 1));

		String xB = Integer.toBinaryString(adapt | xb);
		String yB = Integer.toBinaryString(adapt | yb);
		xB = xB.substring(1);
		yB = yB.substring(1);

		String Z = shuffle(xB, yB);

		int zValue = Integer.parseInt(Z, 2);
		System.out.println("Bits: " + bits + " Point: (" + (int) x + ", " + (int) y + ")" + " Z Value is: " + zValue);
	}

	private String shuffle(String s1, String s2) {
		String s = "";
		int len = s1.length();
		for (int i = 0; i < len; i++) {
			s = s + s1.charAt(i) + s2.charAt(i);
		}
		return s;
	}

	public void ZValueToPoint(int k, int z) {
		int adapt = (int) Math.pow(2.0, (double) k * 2);
		String zb = Integer.toBinaryString(adapt | z).substring(1);

		String xB = "";
		String yB = "";

		for (int i = 0; i < zb.length() - 1; i += 2) {
			xB += zb.charAt(i);
			yB += zb.charAt(i + 1);
		}
		int x = Integer.parseInt(xB, 2);
		int y = Integer.parseInt(yB, 2);
		System.out.println("bits: " + k + " zValue: " + z + " result: (" + x + ", " + y + ")");
	}
}
