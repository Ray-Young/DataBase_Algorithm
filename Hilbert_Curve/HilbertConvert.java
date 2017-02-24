package edu.bu;

/**
 * @author lei
 * This class contains methods to translate Hilbert value to point and point to Hilbert Value
 * 
 * The way Hilbert value to point is based on the idea of the paper: 
 * Encoding and Decoding the Hilbert Order by XIAN LIU AND GUNTHER SCHRACK
 * 
 * The way point to Hilbert value is based on the idea of paper:
 * Crinkly Curves by Brian Hayes
 * 
 * Method declaration
 * HilbertEncode(int bits, int x, int y), translate point to hilbert value
 * hilbertValueToPoint(int bits, int hilbertValue), translate hilbert value to point
 *
 */
public class HilbertConvert {

	private int pixelX;
	private int pixelY;
	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void HilbertEncode(int x, int y, int bits) {
		fitData(x, y, bits);
		processor(bits);
	}

	public void fitData(float x, float y, int bits) {
		float base = 100;
		for (int i = 0; i < bits; i++) {
			base = base / 2;
		}
		pixelX = (int) (x / base);
		pixelY = (int) (y / base);

	}

	public void processor(int bits) {
		int mask = (1 << bits) - 1;
		int interX = 0;
		int interY = pixelX ^ pixelY;
		int tmpx = ~pixelX & mask;
		int tmpy = ~pixelY & mask;
		int temp = tmpx ^ pixelY;

		int v0 = 0, v1 = 0;
		for (int k = 1; k < bits; k++) {
			v1 = ((v1 & interY) | ((v0 ^ tmpy) & temp)) >> 1;
			v0 = ((v0 & (v1 ^ tmpx)) | (~v0 & (v1 ^ tmpy))) >> 1;
		}
		interX = (~v0 & (v1 ^ pixelX)) | (v0 & (v1 ^ tmpy));

		System.out.println(
				"bits:" + bits + ", point: (" + x + ", " + y + ")" + "result: " + translateBits(interX, interY));
	}

	private int translateBits(int interX, int interY) {
		int val = 0;
		int max = Math.max(interX, interY);
		int n = 0;
		while (max > 0) {
			n++;
			max >>= 1;
		}

		for (int i = 0; i < n; i++) {
			int bitMask = 1 << i;
			int a = (interY & bitMask) > 0 ? (1 << (2 * i)) : 0;
			int b = (interX & bitMask) > 0 ? (1 << (2 * i + 1)) : 0;
			val += a + b;
		}

		return val;
	}

	public void hilbertValueToPoint(int n, int d) {
		int rx, ry, s, t = d;
		x = 0;
		y = 0;
		double m = Math.pow(2, (double) n);
		for (s = 1; s < m; s *= 2) {
			rx = 1 & (t / 2);
			ry = 1 & (t ^ rx);
			hilbertRotate(s, rx, ry);
			x = x + s * rx;
			y += s * ry;
			t /= 4;
		}
		System.out.println("Hilbert value: " + d + ", bits: " + n + ", result: (" + x + ", " + y + ")");
	}

	public void hilbertRotate(int n, int rx, int ry) {
		if (ry == 0) {
			if (rx == 1) {
				x = n - 1 - x;
				y = n - 1 - y;
			}

			int tmp = x;
			x = y;
			y = tmp;
		}
	}

}
