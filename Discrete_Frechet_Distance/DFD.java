import java.util.ArrayList;
import java.util.List;

/**
 * @author lei
 * The DFD class contains all related methods for calculating Discrete Frechet Distance
 * Use method df and give two curves' coordinates to get the distance
 * The input should follow the format:
 * x = "2,3;3,4;4,4;3,5;4,5;5,5"
 * y = "2,4;3,5;1,4;2,5;4,6;6,6"
*/
public class DFD {
	private List<float[]> p = new ArrayList<>();
	private List<float[]> q = new ArrayList<>();
	private float[][] ca;

	private void parseData(String P, String Q) {
		String[] pStrs = P.split(";");
		String[] qStrs = Q.split(";");
		for (int i = 0; i < pStrs.length; i++) {
			float[] tmp = new float[2];
			String[] data = pStrs[i].split(",");
			tmp[0] = Float.parseFloat(data[0]);
			tmp[1] = Float.parseFloat(data[1]);
			p.add(tmp);
		}

		for (int i = 0; i < qStrs.length; i++) {
			float[] tmp = new float[2];
			String[] data = qStrs[i].split(",");
			tmp[0] = Float.parseFloat(data[0]);
			tmp[1] = Float.parseFloat(data[1]);
			q.add(tmp);
		}

		ca = new float[pStrs.length][qStrs.length];
		for (int i = 0; i < pStrs.length; i++) {
			for (int j = 0; j < qStrs.length; j++) {
				ca[i][j] = -1;
			}
		}
	}

	private float c(int i, int j) {
		if (ca[i][j] > -1)
			return ca[i][j];
		else if (i == 0 && j == 0)
			ca[i][j] = d(i, j);
		else if (i > 0 && j == 0)
			ca[i][j] = Math.max(c(i - 1, 0), d(i, j));
		else if (i == 0 && j > 0)
			ca[i][j] = Math.max(c(0, j - 1), d(i, j));
		else if (i > 0 && j > 0) {
			ca[i][j] = Math.max(Math.min(Math.min(c(i - 1, j), c(i - 1, j - 1)), c(i, j - 1)), d(i, j));
		} else
			ca[i][j] = Float.MAX_VALUE;
		return ca[i][j];

	}

	private float d(int i, int j) {
		float x = p.get(i)[0] - q.get(j)[0];
		float y = p.get(i)[1] - q.get(j)[1];
		return (float) Math.sqrt(x * x + y * y);
	}

	public float df(String P, String Q) {
		parseData(P, Q);
		return c(p.size() - 1, q.size() - 1);
	}

}
