package fac.android.moodle;

public class User {
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
	
	public User(){
		
	}
	
	public User(String id,String name,String firstName,String lastName, String email, String auth, Boolean confirmed,
			String idNumber, Boolean emailStop, String lang,String theme,String timeZone,Boolean mailFormat,
			String description,String city,String country,String customFields){
		super();
		this.id = id;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.auth = auth;
		this.confirmed = confirmed;
		this.idNumber = idNumber;
		this.emailStop = emailStop;
		this.lang = lang;
		this.theme = theme;
		this.timeZone = timeZone;
		this.mailFormat = mailFormat;
		this.description = description;
		this.city = city;
		this.country = country;
		this.customFields = customFields;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getAuth(){
		return auth;
	}
	
	public void setAuth(String auth){
		this.auth = auth;
	}
	
	public Boolean getConfirmed(){
		return confirmed;
	}
	
	public void setConfirmed(Boolean confirmed){
		this.confirmed = confirmed;
	}
	
	public String getIdNumber(){
		return idNumber;
	}
	
	public void setIdNumber(String idNumber){
		this.idNumber = idNumber;
	}
	
	public Boolean getEmailStop(){
		return emailStop;
	}
	
	public void setEmailStop(Boolean emailStop){
		this.emailStop = emailStop;
	}
	
	public String getLang(){
		return lang;
	}
	
	public void setLang(String lang){
		this.lang = lang;
	}
	
	public String getTheme(){
		return theme;
	}
	
	public void setTheme(String theme){
		this.theme = theme;
	}
	
	public String getTimeZone(){
		return timeZone;
	}
	
	public void setTimeZone(String timeZone){
		this.timeZone = timeZone;
	}
	
	public Boolean getMailFormat(){
		return mailFormat;
	}
	
	public void setMailFormat(Boolean mailFormat){
		this.mailFormat = mailFormat;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getCity(){
		return city;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getCountry(){
		return country;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public String getCustomFields(){
		return customFields;
	}
	
	public void setCustomFields(String customFields){
		this.customFields = customFields;
	}
	

}
