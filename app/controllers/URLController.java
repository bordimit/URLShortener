package controllers;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import models.URLModel;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.*;
import views.html.add;
import views.html.addresponse;
import views.html.all;

public class URLController extends Controller{
	
	// "/"
	public static Result index(){
		return ok(add.render());
	}
	
	// add
	public static Result addURL(){
		URLModel urlm = Form.form(URLModel.class).bindFromRequest().get();
		Pattern p =
		Pattern.compile("http(s)?:\\/\\/.(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		if (p.matcher(urlm.getOriginal()).matches()){
		urlm.setDate(URLModel.generateExpDate());
		urlm.setShortened(URLModel.generateShortened());
		urlm.save();
		String outDate = urlm.getDate().toString();
		String outShortened = urlm.getShortened();
		String response = "Your link was shortened to " +
		outShortened + " and will be valid till " + outDate + "!";
		return ok(addresponse.render(response));
		} 
		else{
		return ok("Your entered link <" + urlm.getOriginal() + "> does not appear to be valid");
		}
	}
	
	// go/:shortened
	// see documentation - problems with validating the urls
	public static Result goToURL(String shortened){
		URLModel urlm = new Model.Finder<String, URLModel>(String.class, URLModel.class).byId(shortened);
		return redirect(urlm.getOriginal());
	}
	
	//list
	public static Result showEmAll(){
		List<URLModel> allURLs = new Model.Finder<String, URLModel>(String.class, URLModel.class).all();
		return ok(all.render(allURLs));
	}
	
	//r/add/:original
	public static Result jsonAddURL(String original){
		String shortened = URLModel.generateShortened();
		Date exp_date = URLModel.generateExpDate();
		String myString = original;	
		URLModel toBeAdded = new URLModel(myString, shortened, exp_date);
		toBeAdded.save();
		ObjectNode result = Json.newObject();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		result.put("original", myString);
		result.put("sh_url", shortened);
		result.put("exp_date", df.format(exp_date));
		return ok(result);
	}
	
	//r/go/:shortened
	public static Result jsonGoToURL(String shortened){
		URLModel urlm = new Model.Finder<String, URLModel>(String.class, URLModel.class).byId(shortened);
		ObjectNode result = Json.newObject();
		result.put("original", urlm.getOriginal());
		return ok(result);
	}
	
	//r/list
	//public static Result jsonShowEmAll(){
		//List<URLModel> allURLs = new Model.Finder<String, URLModel>(String.class, URLModel.class).all();
		//return ok(Json.toJson(allURLs));
	//}
	
	public static Result jsonShowEmAll(){
		List<URLModel> allURLs = new Model.Finder<String, URLModel>(String.class, URLModel.class).all();
		List<ObjectNode> myJSONObjects = new  ArrayList<ObjectNode> (allURLs.size()); 
		for(URLModel a : allURLs) {
		    ObjectNode obj = Json.newObject();
		    obj.put("original",a.getOriginal() );
		    obj.put("shortened", a.getShortened());
		    obj.put("exp_date", a.getDate().toString());
		    myJSONObjects.add(obj);
		}
		return ok(myJSONObjects.toString());
	}
	
	//r/change/:shortened/:updated
	public static Result change(String shortened, String updated){
        URLModel urlm = URLModel.find.byId(shortened);
        if (urlm == null){
        	return ok("There is no URL stored under what you provided!");
        }
        URLModel nurlm = new URLModel(urlm.getOriginal(), updated, urlm.getDate());
        urlm.delete();
        nurlm.save();
	    return ok("The URL which was stored as '" + shortened + "' is now stored as '" + updated + "' !");
	}
}
