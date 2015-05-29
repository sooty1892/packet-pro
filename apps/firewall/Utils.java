public class Utils {

	public static short swap(short value) {
        int b1 = value & 0xff;
        int b2 = (value >> 8) & 0xff;
        return (short) (b1 << 8 | b2 << 0);
    }

	public static int swap(int value) {
    	int b1 = (value >>  0) & 0xff;
    	int b2 = (value >>  8) & 0xff;
    	int b3 = (value >> 16) & 0xff;
    	int b4 = (value >> 24) & 0xff;
		return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
	}

	public static long swap(long value) {
	    long b1 = (value >>  0) & 0xff;
	    long b2 = (value >>  8) & 0xff;
	    long b3 = (value >> 16) & 0xff;
	    long b4 = (value >> 24) & 0xff;
	    long b5 = (value >> 32) & 0xff;
	    long b6 = (value >> 40) & 0xff;
	    long b7 = (value >> 48) & 0xff;
	    long b8 = (value >> 56) & 0xff;
		return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 | b5 << 24 | b6 << 16 | b7 <<  8 | b8 <<  0;
	}

	public static float swap(float value) {
	    int intValue = Float.floatToIntBits(value);
	    intValue = swap(intValue);
	    return Float.intBitsToFloat(intValue);
	}

	public static double swap(double value) {
    	long longValue = Double.doubleToLongBits(value);
    	longValue = swap(longValue);
    	return Double.longBitsToDouble(longValue);
	}

	public static int signToUnsign(byte value) {
		return value & (int)0xFF;
	}

	public static int signToUnsign(short value) {
		return value & (int)0xFFFF;
	}

	public static long signToUnsign(int value) {
		return value & 0xFFFFFFFFL;
	}

	public static String intToIp(long value) {
		StringBuilder sb = new StringBuilder();
		sb.append(((value >> 24) & 0xFF));
		sb.append(".");
		sb.append(((value >> 16) & 0xFF));
		sb.append(".");
		sb.append(((value >> 8) & 0xFF));
		sb.append(".");
		sb.append(((value) & 0xFF));
		return sb.toString();
	}

	public static long IpToInt(String value) {
		String[] parts = value.split("\\.");
		long n = 0;
		for (int i = 0; i < parts.length; i++) {
			int power = 3-i;
			n += Integer.parseInt(parts[i]) % 256 * Math.pow(256, power);
		}
		return n;
	}

}