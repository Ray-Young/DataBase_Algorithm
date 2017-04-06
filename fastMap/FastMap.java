import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * 
 * @author lei This class contains methods for calculating the fastMap
 *         algorithm. Use run method and give proper b value to get the result.
 */
public class FastMap {
	public void run(int b) {
		b = b - 1;
		double[][] d = { { 0, 2, 13, 8, 11, 4, 9 }, { 2, 0, 11, 10, 9, 2, 11 }, { 13, 11, 0, 9, 4, 9, 6 },
				{ 8, 10, 9, 0, 9, 10, 3 }, { 11, 9, 4, 9, 0, 7, 10 }, { 4, 2, 9, 10, 7, 0, 11 },
				{ 9, 11, 6, 3, 10, 11, 0 } };

		System.out.println("The initial input point:" + b + 1);
		int a = getB(b, d);
		b = getB(a, d);
		System.out.println("The first round random points are point: " + (a + 1) + ", and point " + (b + 1));

		double[] x = new double[7];
		double[] x2 = new double[7];

		double[][] mid = new double[7][7];

		for (int i = 0; i < 7; i++) {
			double dai = d[a][i];
			double dab = d[a][b];
			double dbi = d[b][i];

			x[i] = (Math.pow(dai, 2) + Math.pow(dab, 2) - Math.pow(dbi, 2)) / (2 * dab);
		}

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				double answer = produceSolution(d[i][j], x[i], x[j]);
				mid[i][j] = answer;
			}
		}

		b = (int) (Math.random() * 7);
		a = getB(b, mid);
		b = getB(a, mid);

		System.out.println("result: ");
		for (int i = 0; i < 7; i++) {
			double dai = Math.sqrt(mid[a][i]);
			double dab = Math.sqrt(mid[a][b]);
			double dbi = Math.sqrt(mid[b][i]);

			x2[i] = (Math.pow(dai, 2) + Math.pow(dab, 2) - Math.pow(dbi, 2)) / (2 * dab);
			System.out.println("(" + Double.parseDouble(new DecimalFormat("##.##").format(x[i])) + ", "
					+ Double.parseDouble(new DecimalFormat("##.##").format(x2[i])) + ")");
		}
	}

	private double produceSolution(double D, double xi, double xj) {
		double answer = Math.pow(D, 2) - Math.pow((xi - xj), 2);
		return answer;
	}

	private int getB(int a, double[][] m) {
		int maxIndex = 0;
		double max = 0.0;
		for (int i = 0; i < 7; i++) {
			double dist = m[i][a];
			if (dist > max) {
				maxIndex = i;
				max = dist;
			}
		}
		return maxIndex;
	}
}
