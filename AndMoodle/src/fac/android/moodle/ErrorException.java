package fac.android.moodle;

public class ErrorException {
	private String codError;
	private String descError;

	public ErrorException() {

	}

	public ErrorException(String codError, String descError) {
		super();
		this.codError = codError;
		this.descError = descError;
	}

	public String getCodError() {
		return codError;
	}

	public void setCodError(String codError) {
		this.codError = codError;
	}

	public String getDescError() {
		return descError;
	}

	public void setDescError(String descError) {
		this.descError = descError;
	}
	
	public void reset(){
		this.descError = "";
		this.codError = "";
	}

}
