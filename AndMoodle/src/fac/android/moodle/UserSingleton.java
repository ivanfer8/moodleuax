package fac.android.moodle;

public class UserSingleton {
	private String id;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	private String auth;
	private Boolean confirmed;
	private String idNumber;
	private Boolean emailStop;
	private String lang;
	private String theme;
	private String timeZone;
	private Boolean mailFormat;
	private String description;
	private String city;
	private String country;
	private String customFields;
	//este dato es para la creacion
	private String password;

	// SINGLETON DEFINITION
	private static UserSingleton INSTANCE = null;

	private UserSingleton() {
	}

	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserSingleton();
		}
	}

	public static UserSingleton getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}

	// GETTERS AND SETTERS
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Boolean getEmailStop() {
		return emailStop;
	}

	public void setEmailStop(Boolean emailStop) {
		this.emailStop = emailStop;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Boolean getMailFormat() {
		return mailFormat;
	}

	public void setMailFormat(Boolean mailFormat) {
		this.mailFormat = mailFormat;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCustomFields() {
		return customFields;
	}

	public void setCustomFields(String customFields) {
		this.customFields = customFields;
	}
	
	public String getPass() {
		return password;
	}

	public void setPass(String password) {
		this.password = password;
	}
}
