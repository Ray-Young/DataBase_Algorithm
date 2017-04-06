
public class Main {
	public static void main(String[] args) {
		DFD dfd = new DFD();
		String P = "2,3;3,4;4,4;3,5;4,5;5,5";
		String Q = "2,4;3,5;1,4;2,5;4,6;6,6";
		System.out.println(dfd.df(P, Q));
	}

}
