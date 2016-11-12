package models;


import java.security.SecureRandom;
import java.util.*;

import javax.persistence.*;

import play.data.validation.*;
import play.db.ebean.*;
import play.data.format.*;

@Entity
public class URLModel extends Model{
	
	private static final long serialVersionUID = 1L;

	@Constraints.Required
	public String original;
	@Id
	public String shortened;
	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date exp_date;
	
	//constructors
	
	public URLModel(){	
	}
	
	public URLModel(String original, String shortened, Date exp_date){
		this.original = original;
		this.shortened = shortened;
		this.exp_date = exp_date;	
	}
	
	public static Model.Finder<String, URLModel> find = new 
				Model.Finder<String, URLModel>(String.class, URLModel.class);
	
	public void setOriginal(String original){
		this.original = original;
	}
	
	public void setShortened(String shortened){
		this.shortened = shortened;
	}
	
	public void setDate(Date date){
		this.exp_date = date;
	}
	
	public String getShortened(){
		return this.shortened;
	}
	
	public Date getDate(){
		return this.exp_date;
	}
	
	public String getOriginal(){
		return this.original;
	}
	
	public static String generateShortened(){
		//possible characters
		final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		//the length of the generated String is between 8 and 15
		//characters, I think it is good enough 
		int len = rnd.nextInt(8) + 8;
		StringBuilder sb = new StringBuilder(len);
			for( int i = 0; i < len; i++ ) 
				sb.append( AB.charAt(rnd.nextInt(AB.length())));
			return sb.toString();
	}
	
	//sets the expiry date 1 day after the url has been added to the database
	public static Date generateExpDate(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
	}
}
