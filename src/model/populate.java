package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Connection;
import java.sql.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class populate {	
	public static void main(String[] args) throws IOException {
		String business = null;
		String review = null;
		String checkin = null;
		String user = null;
		
		if(args.length == 4){
			business = args[0];
			review = args[1];
			checkin = args[2];
			user = args[3];
		}
		Object obj;
		
		// DB STUFF VARIABLES
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = null;
		Statement st = null;
		String statement;
		
		// TRUNCATE TABLES
		try{    
			st = conn.createStatement();
			st.executeUpdate("TRUNCATE TABLE BUSINESS_HOURS");
			st.executeUpdate("TRUNCATE TABLE CHECK_IN");
			st.executeUpdate("TRUNCATE TABLE REVIEW");
			st.executeUpdate("TRUNCATE TABLE BUSINESS_CATEGORY_SUBCAT");
			st.executeUpdate("DELETE FROM YELP_USER");
			st.executeUpdate("DELETE FROM BUSINESS");

			System.out.println("Tables truncated");
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(ps != null){
                try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        }
			
		// INSERTING TO TABLES
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		BufferedReader br = null;
		String line;
		Path path;
		
		// POPULATE USER
		try {
			path = Paths.get("resources/"+user);
			br = new BufferedReader(new FileReader(path.toFile()));
			
			statement = "INSERT INTO YELP_USER(user_id, name, type, average_stars,"
            		+ "yelping_since, review_count, fan_count, friend_count) VALUES(?,?,?,?,?,?,?,?)";
						
            ps = (PreparedStatement) conn.prepareStatement(statement);
            
            System.out.println("Inserting users");
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("/n", ""); // for yelp_business
				obj = new JSONParser().parse(line);
				JSONObject jo = (JSONObject) obj;
			
				java.util.Date yelping_since = format.parse(jo.get("yelping_since").toString());
				int review_count = (int) ((Long) jo.get("review_count")).intValue();
				String name = (String) jo.get("name");
				String user_id = (String) jo.get("user_id");
				JSONArray friends = (JSONArray) jo.get("friends");
				int friend_count = 0;
				if(friends != null)
					friend_count = friends.size();
				double average_stars = (double) jo.get("average_stars");
				int fan_count = (int) ((Long) jo.get("fans")).intValue();
				String type = (String) jo.get("type");
//				YelpUser u = new YelpUser(user_id, name, type, average_stars, 
//						yelping_since, review_count, fan_count, friend_count);
				
				// ADD TO BATCH INSERTS
				try {
//		            ps.setString(1, u.getUser_id());
//		            ps.setString(2, u.getName());
//		            ps.setString(3, u.getType());
//		            ps.setDouble(4, u.getAverage_stars());
//		            ps.setDate(5, new Date(u.getYelping_since().getTime()));
//		            ps.setInt(6, u.getReview_count());
//		            ps.setInt(7, u.getFan_count());
//		            ps.setInt(8, u.getFriend_count());
					
		            ps.setString(1, user_id);
		            ps.setString(2, name);
		            ps.setString(3,type);
		            ps.setDouble(4, average_stars);
		            ps.setDate(5, new Date(yelping_since.getTime()));
		            ps.setInt(6, review_count);
		            ps.setInt(7, fan_count);
		            ps.setInt(8, friend_count);

		            ps.addBatch();
				}catch (Exception e) {
		            e.printStackTrace();
				}
			}
			ps.executeBatch();
            ps.close();
            System.out.println("Inserted into YELP_USER table!");
            
            
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			br.close();
		}
		
		// POPULATE BUSINESS DETAILS
		try {
			path = Paths.get("resources/"+business);
			br = new BufferedReader(new FileReader(path.toFile()));
			
			statement = "INSERT INTO BUSINESS(business_id, name, full_address, city,"
					+ "state, type, longitude, latitude, open, stars, review_count) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            ps = (PreparedStatement) conn.prepareStatement(statement);
            
            System.out.println("Inserting businesses");
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("/n", ""); // for yelp_business
				obj = new JSONParser().parse(line);
				JSONObject jo = (JSONObject) obj;
				
				String business_id = (String) jo.get("business_id");
				String name = (String) jo.get("name");
				String full_address = (String) jo.get("full_address");
				String city = (String) jo.get("city");
				String state = (String) jo.get("state");
				String type = (String) jo.get("type");
				double longitude = (double) jo.get("longitude");
				double latitude = (double) jo.get("latitude");
				String open = (boolean) jo.get("open") ? "true" : "false";
				double stars = (double) jo.get("stars");
				int review_count = (int) ((Long) jo.get("review_count")).intValue();
//				Business b = new Business(business_id, name, full_address, city, state,
//						type, longitude, latitude, open, stars, review_count);
					
				// ADD TO BATCH INSERTS
				try {
//		            ps.setString(1, b.getBusiness_id());
//		            ps.setString(2,b.getName());
//		            ps.setString(3, b.getFull_address());
//		            ps.setString(4, b.getCity());
//		            ps.setString(5, b.getState());
//		            ps.setString(6, b.getType());
//		            ps.setDouble(7, b.getLongitude());
//		            ps.setDouble(8, b.getLatitude());
//		            ps.setString(9,  b.getOpen());
//		            ps.setDouble(10, b.getStars());
//		            ps.setInt(11, b.getReview_count());

					ps.setString(1, business_id);
		            ps.setString(2, name);
		            ps.setString(3, full_address);
		            ps.setString(4, city);
		            ps.setString(5, state);
		            ps.setString(6, type);
		            ps.setDouble(7, longitude);
		            ps.setDouble(8, latitude);
		            ps.setString(9, open);
		            ps.setDouble(10, stars);
		            ps.setInt(11, review_count);
		            ps.addBatch();
		    	
				}catch (Exception e) {
		            e.printStackTrace();
				}
			}
			ps.executeBatch();
            ps.close();
            System.out.println("Inserted into BUSINESS table!");
            
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			br.close();
		}
		
		System.out.println("Inserting business-related info");
		// POPULATE BUSINESS-RELATED DETAILS
		try {
			path = Paths.get("resources/"+business);
			br = new BufferedReader(new FileReader(path.toFile()));
			
			String stmnt_hours = "INSERT INTO BUSINESS_HOURS(business_id, day, hour, type) VALUES(?,?,?,?)";
			String stmnt_cat = "INSERT INTO BUSINESS_CATEGORY_SUBCAT(business_id,category_name, subcategory_name) VALUES(?,?,?)";
			
            PreparedStatement ps_hours = (PreparedStatement) conn.prepareStatement(stmnt_hours);
            PreparedStatement ps_cat = (PreparedStatement) conn.prepareStatement(stmnt_cat);
            
            List<String> categories = new ArrayList<>();
            categories.add("Active Life");
            categories.add("Arts & Entertainment");
            categories.add("Automotive");
            categories.add("Car Rental");
            categories.add("Cafes");
            categories.add("Beauty & Spas");
            categories.add("Convenience Stores");
            categories.add("Dentists");
            categories.add("Doctors");
            categories.add("Drugstores");
            categories.add("Department Stores");
            categories.add("Education");
            categories.add("Event Planning & Services");
            categories.add("Flowers & Gifts");
            categories.add("Food");
            categories.add("Health & Medical");
            categories.add("Home Services");
            categories.add("Home & Garden");
            categories.add("Hospitals");
            categories.add("Hotels & Travel");
            categories.add("Hardware Stores");
            categories.add("Grocery");
            categories.add("Medical Centers");
            categories.add("Nurseries & Gardening");
            categories.add("Nightlife");
            categories.add("Restaurants");
            categories.add("Shopping");
            categories.add("Transportation");
         
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("/n", ""); // for yelp_business
				obj = new JSONParser().parse(line);
				JSONObject jo = (JSONObject) obj;
				
				JSONObject hours = (JSONObject) jo.get("hours");
				JSONArray cat = (JSONArray) jo.get("categories");
				String business_id = (String) jo.get("business_id");
				
				// BUSINESS HOURS
				String open_hour;
				String close_hour;
				String day;
				for(Object s: hours.keySet()){
					day = (String) s;
					open_hour = (String)((JSONObject) hours.get(s)).get("open");
					close_hour = (String)((JSONObject) hours.get(s)).get("close");
					
					// add to ps_hours -- opening 
					ps_hours.setString(1, business_id);
					ps_hours.setString(2, day);
					ps_hours.setString(3, open_hour);
					ps_hours.setString(4, "open");
					ps_hours.addBatch();
					
					// add to ps_hours -- closing 
					ps_hours.setString(1, business_id);
					ps_hours.setString(2, day);
					ps_hours.setString(3, close_hour);
					ps_hours.setString(4, "close");
					ps_hours.addBatch();	
				}
				
				// BUSINESS CATEGORIES
				Set<String> main = new HashSet();
				Set<String> sub = new HashSet();
				
				for(Object o:cat){
					String c = (String) o;
					if(categories.contains(c)){
						// add to BUSINESS CATEGORY
						main.add(c);
					} else {
						// add to BUSINESS SUBCATEGORY
						sub.add(c);
					}
				}
				
				if(main.size() > 0){
					// add to BUSINESS_CATEGORY_SUBCAT table
					for(String m : main){
						for(String subcat : sub){
							ps_cat.setString(1, business_id);
							ps_cat.setString(2, m);
							ps_cat.setString(3, subcat);
							ps_cat.addBatch();
						}
					}
				} else {
					for(String subcat : sub){
						ps_cat.setString(1, business_id);
						ps_cat.setString(2, "General");
						ps_cat.setString(3, subcat);
						ps_cat.addBatch();
					}
				}
			}
			ps_hours.executeBatch();
			ps_hours.close();
			ps_cat.executeBatch();
			ps_cat.close();
			
            System.out.println("Inserted into BUSINESS-related tables!");
          
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			br.close();
		}
		
		// Populate CHECK-IN 
		try {
			path = Paths.get("resources/"+checkin);
			br = new BufferedReader(new FileReader(path.toFile()));
			
			statement = "INSERT INTO CHECK_IN(business_id, type, day, hour, count) "
					+ "VALUES (?,?,?,?,?)";
            ps = (PreparedStatement) conn.prepareStatement(statement);
            
            System.out.println("Inserting check-ins");
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("/n", ""); // for yelp_business
				obj = new JSONParser().parse(line);
				JSONObject jo = (JSONObject) obj;
				
				String business_id = (String) jo.get("business_id");
				String type = (String) jo.get("type");
				
				String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
				// get keySet
				// parse keySet : hour-day
				// value = count
				JSONObject info = (JSONObject) jo.get("checkin_info");
				for(Object s: info.keySet()){
					String hour_day = (String) s;
					String[] tmp = hour_day.split("-");
					int checkin_hour = Integer.parseInt(tmp[0]);
					int checkin_day = Integer.parseInt(tmp[1]);
					int count = (int)((Long) info.get(s)).intValue(); 
					
					ps.setString(1, business_id);
					ps.setString(2, type);
					ps.setInt(3, checkin_day);
					ps.setInt(4, checkin_hour);
					ps.setInt(5, count);
					ps.addBatch();
				}
			}
			ps.executeBatch();
            ps.close();
            System.out.println("Inserted into CHECKIN table!");
            
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			br.close();
			db.disconnect();
		}
		
		// POPULATE REVIEW
		int count=1;
		conn = db.getConnection();
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-DD");
		JSONParser jsonParser = new JSONParser();
		try {
			path = Paths.get("resources/"+review);
			br = new BufferedReader(new FileReader(path.toFile()));
			
			statement = "INSERT INTO REVIEW(review_id, user_id, business_id, publish_date,"
					+ "text, type, stars, votes) "
					+ "VALUES(?,?,?,?,?,?,?,?)";
            ps = (PreparedStatement) conn.prepareStatement(statement);
            
            System.out.println("Inserting reviews");
			while ((line = br.readLine()) != null) {
//            line="{\"votes\": {\"funny\": 0, \"useful\": 2, \"cool\": 1}, "
//            		+ "\"user_id\": \"Xqd0DzHaiyRqVH3WRG7hzg\", "
//            		+ "\"review_id\": \"15SdjuK7DmYqUAj6rjGowg\", "
//            		+ "\"stars\": 5, \"date\": \"2007-05-17\", "
//            		+ "\"text\": \"dr. goldberg offers everything i look for in a general practitioner.  he's nice and easy to talk to without being patronizing; he's always on time in seeing his patients; he's affiliated with a top-notch hospital (nyu) which my parents have explained to me is very important in case something happens and you need surgery; and you can get referrals to see specialists without having to see him first.  really, what more do you need?  i'm sitting here trying to think of any complaints i have about him, but i'm really drawing a blank.\", "
//            		+ "\"type\": \"review\", \"business_id\": \"vcNAWiLM4dR7D2nwwJ7nCA\"}";
				count++;
				if(count % 100000 == 0){
					ps.executeBatch();
					ps.close();
					ps = (PreparedStatement) conn.prepareStatement(statement);
					System.out.println("Batch "+count/100000 + " done");
				}
				line = line.replaceAll("/n", ""); // for yelp_business
				obj = jsonParser.parse(line);
				JSONObject jo = (JSONObject) obj;
				
				String review_id = (String) jo.get("review_id");
				String user_id = (String) jo.get("user_id");
				String business_id = (String) jo.get("business_id");
				java.util.Date publish_date = format2.parse(jo.get("date").toString());
				String text = (String) jo.get("text");
				String type = (String) jo.get("type");
				int stars = (int) ((Long) jo.get("stars")).intValue();
		
				JSONObject votes = (JSONObject) jo.get("votes");
				int useful_votes = (int) ((Long) votes.get("useful")).intValue();
				int funny_votes = (int) ((Long) votes.get("funny")).intValue();
				int cool_votes = (int) ((Long) votes.get("cool")).intValue();
				
				// ADD TO BATCH INSERTS
				try {
		            ps.setString(1, review_id);
		            ps.setString(2, user_id);
		            ps.setString(3, business_id);
		            ps.setDate(4, new Date(publish_date.getTime()));
		            ps.setString(5, text);
		            ps.setString(6, type);
		            ps.setInt(7, stars);
		            ps.setInt(8, useful_votes+funny_votes+cool_votes);
		            ps.addBatch();
		    	
				}catch (Exception e) {
		            e.printStackTrace();
				}
			}
			ps.executeBatch();
            ps.close();
            System.out.println("Inserted into REVIEW table!");
            
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
			br.close();
			db.disconnect();
		}
	}
}
