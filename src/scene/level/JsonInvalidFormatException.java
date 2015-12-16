package scene.level;

public class JsonInvalidFormatException extends Exception {

	private boolean isValid;

	public JsonInvalidFormatException(boolean isValid) {
		this.isValid = isValid;
	}

	public JsonInvalidFormatException(String message , boolean isValid) {
		super(message);
		this.isValid = isValid;
	}

	@Override
	public String getMessage() {
		if (!isValid)
			return "Input JSON String has invalid format";
		else
			return "";
	}
}
