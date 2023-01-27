package homework;

public class Utilities {
	public static double round(double number) {
		return round(number, 2);
	}

	public static double round(double number, int decimalPoints) {
		double factor = Math.pow(10, decimalPoints);
		return Math.round(number * factor) / factor;
	}

	public static boolean isPrime(int n) {
		if (n <= 1) {
			return false;
		}
		for (int i = 2; i < Math.sqrt(n); i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
}
