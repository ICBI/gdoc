

class EncryptionUtil {
	final static byte[] salt = [(byte) 0xF9, (byte) 0xAB, (byte) 0xE8,
			(byte) 0xF2, (byte) 0x5D, (byte) 0x3A, (byte) 0xEF, (byte) 0x03 ];

	final static int iterationCount = 23;
	private final static String PRIVATE_KEY = "G-DOC Saxa Hoya Database ";
	private static DESEncrypter encrypter;

	static {
		encrypter = new DESEncrypter(PRIVATE_KEY,salt,iterationCount);
	}
	

	static encrypt(String message) {
		return encrypter.encrypt(message);
	}

	static decrypt(String message) {
		return encrypter.decrypt(message);
	}
	
}
