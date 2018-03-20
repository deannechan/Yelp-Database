package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class DBQueries {
	
	DBConnection db = new DBConnection();
	Connection conn = db.getConnection();
	PreparedStatement ps = null;
	String statement;
	
	private static HashMap<String, Integer> daysConversion = new HashMap<>();
	
	public DBQueries(){
		daysConversion.put("Sunday", 0);
		daysConversion.put("Monday", 1);
		daysConversion.put("Tuesday", 2);
		daysConversion.put("Wednesday", 3);
		daysConversion.put("Thursday", 4);
		daysConversion.put("Friday", 5);
		daysConversion.put("Saturday", 6);
	}
	// get categories
	public ArrayList<String> getCategories(){
		 db = new DBConnection();
		 conn = db.getConnection();
		 ps = null;
		 ArrayList<String> categories = new ArrayList<>();
		 String s;
		 
		 try{
			 statement = "SELECT DISTINCT category_name FROM BUSINESS_CATEGORY_SUBCAT";
			 ps = conn.prepareStatement(statement);
			 ResultSet rs = ps.executeQuery();
	     
	         while (rs.next()) {
	        	 s = rs.getString("category_name");
	             categories.add(s);
	         }
	         rs.close();
	         ps.close();
	         
		 } catch(SQLException e){
			 e.printStackTrace();
		 } finally{
			 if(ps != null){
				 try {
					ps.close();
				 } catch (SQLException e) {
					e.printStackTrace();
				 }
	         }
			 db.disconnect();
		 }
		 categories.remove("General");
		return categories;
	}
	
	// get subcategories
	public ArrayList<String> getSubcategories(ArrayList<String> category){
		ArrayList<String> subcats = new ArrayList<>();
		db = new DBConnection();
		conn = db.getConnection();
		ps = null;
		String s;
		 
		try{
			if(category.size() > 0){
				category.add("General");
				statement = createQuery(category.size());
				ps = conn.prepareStatement(statement);
				for(int i = 1; i <= category.size(); i++){
					ps.setString(i, category.get(i-1));
				}
				ResultSet rs = ps.executeQuery();
			     
		        while (rs.next()) {
		        	s = rs.getString("subcategory_name");
		            subcats.add(s);
		        }
		        rs.close();
		        ps.close();
			}else {
				return subcats;
			}
	         
		 } catch(SQLException e){
			e.printStackTrace();
		 } finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
			db.disconnect();
		 }
		return subcats;
	}
	
	// get subcategory name where category_name IN
	private static String createQuery(int length) {
		String query = "SELECT DISTINCT subcategory_name FROM BUSINESS_CATEGORY_SUBCAT WHERE category_name IN (";
		StringBuilder queryBuilder = new StringBuilder(query);
		for( int i = 0; i< length; i++){
			queryBuilder.append(" ?");
			if(i != length -1) queryBuilder.append(",");
		}
		queryBuilder.append(") ORDER BY subcategory_name ASC");
		return queryBuilder.toString();
	}
	
	// get business_id where category_name IN
	private static String createBQuery(int catLen, int subLen){
		String query = "SELECT DISTINCT business_id FROM BUSINESS_CATEGORY_SUBCAT WHERE category_name IN (";
		StringBuilder queryBuilder = new StringBuilder(query);
		for( int i = 0; i< catLen; i++){
			queryBuilder.append(" ?");
			if(i != catLen -1) queryBuilder.append(",");
		}
		queryBuilder.append(") AND subcategory_name IN (");
		for( int i = 0; i< subLen; i++){
			queryBuilder.append(" ?");
			if(i != subLen -1) queryBuilder.append(",");
		}
		queryBuilder.append(")");
		return queryBuilder.toString();
	}
	
	// get business_ids given a list of categories and subcategories
	public ArrayList<String> getBusinesses(ArrayList<String> categories, ArrayList<String> subcats){
		ArrayList<String> b_ids = new ArrayList<>();
		db = new DBConnection();
		conn = db.getConnection();
		ps = null;
		String s;
		 
		try{
			if(categories.size() > 0){
				statement = createBQuery(categories.size(), subcats.size());
				ps = conn.prepareStatement(statement);
				for(int i = 1; i <= categories.size(); i++){
					ps.setString(i, categories.get(i-1));
				}
				for(int i = categories.size()+1; i <= categories.size()+subcats.size(); i++){
					ps.setString(i, subcats.get(i-categories.size()-1));
				}
				ResultSet rs = ps.executeQuery();
			     
		        while (rs.next()) {
		        	s = rs.getString("business_id");
		        	b_ids.add(s);
		        }
		        rs.close();
		        ps.close();
			}else {
				return b_ids;
			}
	         
		 } catch(SQLException e){
			e.printStackTrace();
		 } finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
			db.disconnect();
		 }	
		return b_ids;
		
	}
	
	// day hour checkin times for businesses with selected categories and subcategories
	private static String createQuery2(int catLen, int subLen){
		String query = "SELECT DISTINCT day, hour FROM CHECK_IN WHERE business_id IN ("
				+ "SELECT DISTINCT business_id FROM BUSINESS_CATEGORY_SUBCAT WHERE category_name IN (";
		StringBuilder queryBuilder = new StringBuilder(query);
		for( int i = 0; i< catLen; i++){
			queryBuilder.append("?");
			if(i != catLen -1) queryBuilder.append(",");
		}
		if(subLen > 0) {
			queryBuilder.append(") AND subcategory_name IN (");
			for( int i = 0; i< subLen; i++){
				queryBuilder.append("?");
				if(i != subLen -1) queryBuilder.append(",");
			}
		}
		
		queryBuilder.append(")) ORDER BY day ASC");
		return queryBuilder.toString();
	}
	
	// day hour checkin times for businesses with selected categories and subcategories
	public HashMap<Integer, ArrayList<Integer>> getCheckInTimes(ArrayList<String> categories, ArrayList<String> subcats){
		db = new DBConnection();
		conn = db.getConnection();
		ps = null;
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
		
		try{
			if(categories.size() > 0){
				statement = createQuery2(categories.size(), subcats.size());
				ps = conn.prepareStatement(statement);
				for(int i = 1; i <= categories.size(); i++){
					ps.setString(i, categories.get(i-1));
				}
				for(int i = categories.size()+1; i <= categories.size()+subcats.size(); i++){
					ps.setString(i, subcats.get(i-categories.size()-1));
				}
				ResultSet rs = ps.executeQuery();
//			    System.out.println(rs.getStatement());
			    
		        while (rs.next()) {
		        	int d = rs.getInt("day");
		        	int h = rs.getInt("hour");
		        	
		        	if(!map.containsKey(d)){
		        		map.put(d, new ArrayList<>());
		        	}
		        	map.get(d).add(h);
		        }
		        rs.close();
		        ps.close();
			}
	         
		 } catch(SQLException e){
			e.printStackTrace();
		 } finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
			db.disconnect();
		 }
		return map;
	}
	
	public static String createReviewQuery(Date from_review, Date to_review, String b_starsOp, String b_stars,
			String b_votesOp, String b_votes) throws ParseException{
		ArrayList<StringBuilder> attr = new ArrayList<StringBuilder>();
		
		StringBuilder queryBuilder = new StringBuilder();
		
		if(!b_stars.equals("")){
			StringBuilder starz = new StringBuilder();
			starz.append("stars ").append(b_starsOp).append(b_stars);
			attr.add(starz);
		}
		if(!b_votes.equals("")){
			StringBuilder bvotes = new StringBuilder();
			bvotes.append("votes").append(b_votesOp).append(b_votes);
			attr.add(bvotes);
		}
		
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yy");
		
		if(from_review != null && to_review != null){ // both from and to
			int fromMonth = from_review.getMonth()+1;
			int fromYear = from_review.getYear()+1900;
			int fromDay = from_review.getDate();
			
			int toMonth = to_review.getMonth()+1;
			int toYear = to_review.getYear()+1900;
			int toDay = to_review.getDate();
			
			String fromDate = fromYear+"-"+fromMonth+"-"+fromDay;
			String toDate = toYear+"-"+toMonth+"-"+toDay;
			String fromParsedDate = output.format(input.parse(fromDate)).toString().toUpperCase();
			String toParsedDate = output.format(input.parse(toDate)).toString().toUpperCase();
			
			StringBuilder reviewDatez = new StringBuilder();
			reviewDatez.append("publish_date >= ").append("TO_DATE('").append(fromParsedDate).append("','dd-MON-yy')");
			reviewDatez.append(" AND publish_date <= ").append("TO_DATE('").append(toParsedDate).append("','dd-MON-yy')");
			attr.add(reviewDatez);
			
		} else if(from_review != null) { //only from 
			int fromMonth = from_review.getMonth()+1;
			int fromYear = from_review.getYear()+1900;
			int fromDay = from_review.getDate();
			
			String fromDate = fromYear+"-"+fromMonth+"-"+fromDay;
			String fromParsedDate = output.format(input.parse(fromDate)).toString().toUpperCase();
			
			StringBuilder reviewDatez = new StringBuilder();
			reviewDatez.append("publish_date >= ").append("TO_DATE('").append(fromParsedDate).append("','dd-MON-yy')");
			attr.add(reviewDatez);
			
		} else if(to_review != null){ // only to
			int toMonth = to_review.getMonth()+1;
			int toYear = to_review.getYear()+1900;
			int toDay = to_review.getDate();
			
			String toDate = toYear+"-"+toMonth+"-"+toDay;
			String toParsedDate = output.format(input.parse(toDate)).toString().toUpperCase();
			
			StringBuilder reviewDatez = new StringBuilder();
			reviewDatez.append("publish_date <= ").append("TO_DATE('").append(toParsedDate).append("','dd-MON-yy')");
			attr.add(reviewDatez);
			
		}
		if(attr.size() > 0){
			queryBuilder.append("(SELECT business_id FROM REVIEW");	
			queryBuilder.append(" WHERE ");
			for(int i=0; i<attr.size(); i++){
				queryBuilder.append(attr.get(i));
				if(i != attr.size()-1) queryBuilder.append(" AND ");
			}
			queryBuilder.append(")");
		}
		return queryBuilder.toString();
	}
	
	private static String createCheckInQuery(String from_day, String to_day,
			String from_hour, String to_hour,
			String numCheckinOp, String numCheckin){
		boolean isFromDay = from_day.equals("Days") ? false : true;
		boolean isToDay = to_day.equals("Days") ? false : true;
		boolean isFromHour = (from_hour == null || from_hour.equals("Hours")) ? false : true;
		boolean isToHour = (to_hour == null || to_hour.equals("Hours")) ? false : true;
		boolean isNumCheckin = numCheckin.trim().equals("") ? false : true;
		
		boolean checkFirst = true;
		
		StringBuilder qb = new StringBuilder();
		String select = "SELECT business_id, SUM(count) FROM CHECK_IN WHERE ";
		String dayIn = "day IN (";
		String allBId = "SELECT business_id, SUM(count) FROM CHECK_IN";
		if(isFromDay || isToDay){
			qb.append("(SELECT business_id FROM (");	
			if(isFromDay && isToDay){ 
				int day1 = daysConversion.get(from_day);
				int day2 = daysConversion.get(to_day);
				int dayCount = 0;
				if(day1 <= day2)
					dayCount = day2-day1+1;
				else{
					dayCount = 8-day1+day2;
				}
				
				
				if(isFromHour && isToHour){ // has both hours	
					if(dayCount > 2){
						checkFirst = false;
						qb.append(select);
						qb.append(dayIn);
					}	
					for(int i=1; i<dayCount-1; i++){
						int tmp = (day1+i)%7;
						qb.append(tmp); // days excluding fromDay and toDay
						if(i != dayCount-2) qb.append(",");
					}
					if(dayCount > 2) qb.append(") OR "); // day IN ()
					//fromDay and fromHour
					if(checkFirst)
						qb.append(select);
					qb.append("(day=").append(day1);
					qb.append(" AND hour >=").append(from_hour.split(":")[0]).append(")");
					
					qb.append(" OR ");
					//toDay and toHour
					qb.append("(day=").append(day2);
					qb.append(" AND hour <=").append(to_hour.split(":")[0]).append(")");
					
				} else if (isFromHour) { // only has from hour
					qb.append(select);
					qb.append(dayIn);
					
					for(int i=1; i<dayCount; i++){
						int tmp = (day1+i)%7;
						qb.append(tmp);
						if(i != dayCount-1) qb.append(",");
					}
					qb.append(") OR ");
					qb.append("(day=").append(day1);
					qb.append(" AND hour >=").append(from_hour.split(":")[0]).append(")");
					
				} else if (isToHour) {
					qb.append(select);
					qb.append(dayIn);
					
					for(int i=0; i<dayCount-1; i++){
						int tmp = (day1+i)%7;
						qb.append(tmp);
						if(i != dayCount-2) qb.append(",");
					}
					qb.append(") OR ");
					
					//toHour
					qb.append("(day=").append(day2);
					qb.append(" AND hour <=").append(to_hour.split(":")[0]).append(")");
					
				} else { // fromDay and toDay only
					qb.append(select);
					qb.append(dayIn);
					
					for(int i=0; i<dayCount; i++){
						int tmp = (day1+i)%7;
						qb.append(tmp);
						if(i != dayCount-1) qb.append(",");
					}
					qb.append(")");
				}
			} else if(isFromDay){ // only from day 
				if(isFromHour){
					int dayCount = 7;
					int day1 = daysConversion.get(from_day);
					
					qb.append(select);
					qb.append(dayIn);
					for(int i=1; i<dayCount; i++){
						int tmp = (day1+i)%7;
						qb.append(tmp);
						if(i != dayCount-1) qb.append(",");
					}
					qb.append(") OR ");
					
					//fromHour
					qb.append("(day=").append(day1);
					qb.append(" AND hour >=").append(from_hour.split(":")[0]).append(")");
					
				} else
					qb.append(allBId);
			} else if(isToDay){ // only to day
				if(isToHour){
					int dayCount = 7;
					int day2 = daysConversion.get(to_hour.split(":")[0]);
					
					qb.append(select);
					qb.append(dayIn);
					for(int i=1; i<dayCount; i++){
						int tmp = (day2+i)%7;
						qb.append(tmp);
						if(i != dayCount-1) qb.append(",");
					}
					qb.append(") OR ");
					
					//fromHour
					qb.append("(day=").append(day2);
					qb.append(" AND hour <=").append(to_hour.split(":")[0]).append(")");
				} else
					qb.append(allBId);
			}
			qb.append(" GROUP BY business_id");
			if(isNumCheckin)
				qb.append(" HAVING SUM(count)").append(numCheckinOp).append(numCheckin.trim());
			
			qb.append("))");
			return qb.toString();	
		}else if(isNumCheckin){ 
			qb.append("(SELECT business_id FROM (SELECT business_id, SUM(count) FROM CHECK_IN GROUP BY business_id HAVING SUM(count)")
				.append(numCheckinOp).append(numCheckin.trim()).append("))");
			return qb.toString();
		}else
			return "";
	}
	
	private static String createCategoryQuery(ArrayList<String> categories, ArrayList<String> subcats){
		StringBuilder queryBuilder = new StringBuilder();
		if(categories.size() > 0){
			queryBuilder.append("(SELECT DISTINCT business_id FROM BUSINESS_CATEGORY_SUBCAT WHERE category_name IN (");
			for(int j = 0; j < categories.size(); j++){
				queryBuilder.append("?");//categories.get(j));
				if(j != categories.size()-1) queryBuilder.append(",");
			}	
			if(subcats.size() > 0) {
				queryBuilder.append(") AND subcategory_name IN (");
				for(int j = 0; j < subcats.size(); j++){
					queryBuilder.append("?");//subcats.get(j));
					if(j != subcats.size()-1) queryBuilder.append(",");
				}
			}
			queryBuilder.append("))");
		} 
		return queryBuilder.toString();
	}
	// return businesses with selected attributes
	private static String createQuery3(ArrayList<String> categories, ArrayList<String> subcats, 
			String from_day, String to_day,
			String from_hour, String to_hour,
			String numCheckinOp, String numCheckin,
			Date from_review, Date to_review,
			String b_starsOp, String b_stars,
			String b_votesOp, String b_votes){
		StringBuilder queryBuilder = new StringBuilder();
		boolean isIntersect = false;
		
		String category = createCategoryQuery(categories, subcats);
		String checkin = createCheckInQuery(from_day, to_day, from_hour, to_hour, numCheckinOp, numCheckin);
		String review = "";
		try {
			review = createReviewQuery(from_review, to_review, b_starsOp, b_stars, b_votesOp, b_votes);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		queryBuilder.append("SELECT * FROM BUSINESS WHERE business_id IN (");
		if(category.equals("") && checkin.equals("") && review.equals(""))
			return "SELECT * FROM BUSINESS";
		
		if(!category.equals("")){
			queryBuilder.append(category);
			isIntersect = true;
		}
		if(!checkin.equals("")){
			if(isIntersect) queryBuilder.append(" INTERSECT ");
			queryBuilder.append(checkin);
		}
		if(!review.equals("")){
			if(isIntersect) queryBuilder.append(" INTERSECT ");
			queryBuilder.append(review);
		}
		
		queryBuilder.append(")");
		return queryBuilder.toString();
	}
	
	
	public String getFullBusinessQuery(ArrayList<String> categories, ArrayList<String> subcats, 
			String from_day, String to_day,
			String from_hour, String to_hour,
			String numCheckinOp, String numCheckin,
			Date from_review, Date to_review,
			String b_starsOp, String b_stars,
			String b_votesOp, String b_votes){
			
		StringBuilder queryBuilder = new StringBuilder();
		boolean isIntersect = false;
		
		String checkin = createCheckInQuery(from_day, to_day, from_hour, to_hour, numCheckinOp, numCheckin);
		String review = "";
			
		try {
			review = createReviewQuery(from_review, to_review, b_starsOp, b_stars, b_votesOp, b_votes);
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		queryBuilder.append("SELECT * FROM BUSINESS WHERE business_id IN (");
		if(categories.size() == 0 && checkin.equals("") && review.equals(""))
			return "SELECT * FROM BUSINESS";
		else if(categories.size() > 0){
			isIntersect = true;
			queryBuilder.append("(SELECT DISTINCT business_id FROM BUSINESS_CATEGORY_SUBCAT WHERE category_name IN (");
		
			for(int j = 0; j < categories.size(); j++){
				queryBuilder.append(categories.get(j));
				if(j != categories.size()-1) queryBuilder.append(",");
			}	
				
			if(subcats.size() > 0) {
				queryBuilder.append(") AND subcategory_name IN (");
				for(int j = 0; j < subcats.size(); j++){
					queryBuilder.append(subcats.get(j));
					if(j != subcats.size()-1) queryBuilder.append(",");
				}
			}
			queryBuilder.append("))"); // subcategory + (SELECT..
		}
		if(!checkin.equals("")){
			if(isIntersect) queryBuilder.append(" INTERSECT ");
			queryBuilder.append(checkin);
		}
		if(!review.equals("")){
			if(isIntersect) queryBuilder.append(" INTERSECT ");
			queryBuilder.append(review);
		}
		
		queryBuilder.append(")");
		return queryBuilder.toString();
	}
	
	// get businesses query
	public DefaultTableModel getBusinessesQuery(ArrayList<String> categories, ArrayList<String> subcats,
			String from_day, String to_day,
			String from_hour, String to_hour,
			String numCheckinOp, String numCheckin,
			Date from_review, Date to_review,
			String b_starsOp, String b_stars,
			String b_votesOp, String b_votes){
		db = new DBConnection();
		conn = db.getConnection();
		ps = null;
		DefaultTableModel d = null;
		
		try{
			statement = createQuery3(categories, subcats, from_day, to_day,
						from_hour, to_hour, numCheckinOp, numCheckin, from_review, to_review,
						b_starsOp, b_stars, b_votesOp, b_votes);
			System.out.println(statement);
			
			ps = conn.prepareStatement(statement);
			
			if(categories.size() > 0){ 
				for(int i = 1; i <= categories.size(); i++){
					ps.setString(i, categories.get(i-1));
				}
				for(int i = categories.size()+1; i <= categories.size()+subcats.size(); i++){
					ps.setString(i, subcats.get(i-categories.size()-1));
				}
			}
			
			ResultSet rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();

            Vector<String> columnNames = new Vector<String>();
//            int columnCount = metaData.getColumnCount();
//            for (int column = 1; column <= columnCount; column++) 
//            {
//                columnNames.add(metaData.getColumnName(column));
//            }
            columnNames.add("Business ID");
            columnNames.add("Name");
            columnNames.add("City");
            columnNames.add("State");
            columnNames.add("Stars");
            columnNames.add("Review Count");
            columnNames.add("Open");
            
            String[] attr = {"business_id","name", "city","state", "stars", "review_count", "open"};
	        
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) 
            {
            	Vector<Object> vector = new Vector<Object>();
            	for(String at: attr){
            		vector.add(rs.getObject(at));
            	}
            	data.add(vector);
            }
            
            d = new DefaultTableModel(data, columnNames);
	        rs.close();
	        ps.close();
		 } catch(SQLException e){
			e.printStackTrace();
		 } finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
			db.disconnect();
		 }
		return d;
		
	}
	
	public static String createUserQuery(String reviewOp, String reviewCnt, String friendOp, String friendCnt, 
			String starOp, String starCnt, String andOr, Date startMember) throws ParseException{
		ArrayList<StringBuilder> attr = new ArrayList<StringBuilder>();
		
		String query = "SELECT * FROM YELP_USER";
		StringBuilder queryBuilder = new StringBuilder(query);
		if(!reviewCnt.equals("")){
			StringBuilder review = new StringBuilder();
			review.append("review_count ").append(reviewOp).append(reviewCnt);
			attr.add(review);
		}
		if(!friendCnt.equals("")){
			StringBuilder friend = new StringBuilder();
			friend.append("friend_count ").append(friendOp).append(friendCnt);
			attr.add(friend);
		}
		if(!starCnt.equals("")){
			StringBuilder star = new StringBuilder();
			star.append("average_stars ").append(starOp).append(starCnt);
			attr.add(star);
		}
		if(startMember != null){
			SimpleDateFormat input = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yy");
			int month = startMember.getMonth()+1;
			int year = startMember.getYear()+1900;
			
			String d = year+"-"+month;
			String x = output.format(input.parse(d)).toString().toUpperCase();
			
			StringBuilder yelping = new StringBuilder();
			yelping.append("yelping_since >= ").append("TO_DATE('").append(x).append("','dd-MON-yy')");
			attr.add(yelping);
		}
		if(attr.size() > 0){
			queryBuilder.append(" WHERE ");
			for(int i=0; i<attr.size(); i++){
				queryBuilder.append(attr.get(i));
				if(i != attr.size()-1) queryBuilder.append(" ").append(andOr).append(" ");
			}
		}
		return queryBuilder.toString();
	}
	
	public DefaultTableModel getUsersQuery(String reviewOp, String reviewCnt, String friendOp, String friendCnt, 
			String starOp, String starCnt, String andOr, Date startMember){
		db = new DBConnection();
		conn = db.getConnection();
		ps = null;
		DefaultTableModel d = null;
		try{
			
			statement = createUserQuery(reviewOp, reviewCnt, friendOp, friendCnt, starOp, starCnt, andOr, startMember);
			System.out.println(statement);
			ps = conn.prepareStatement(statement);
			ResultSet rs = ps.executeQuery();
				
			ResultSetMetaData metaData = rs.getMetaData();

	        Vector<String> columnNames = new Vector<String>();
	        columnNames.add("User ID");
	        columnNames.add("Name");
	        columnNames.add("Ave. Stars");
	        columnNames.add("Review Count");
	        columnNames.add("# of Friends");
	        columnNames.add("Member Since");
	        
	        String[] attr = {"user_id", "name", "average_stars","review_count", "friend_count", "yelping_since"};
	        
	        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	        while (rs.next()){
	            Vector<Object> vector = new Vector<Object>();
	            for(String at : attr){
	            	vector.add(rs.getObject(at));
	            }
	            data.add(vector);
	        }
	            
	        d = new DefaultTableModel(data, columnNames);
		    rs.close();
		    ps.close();
	         
		 } catch(SQLException e){
			e.printStackTrace();
		 } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
			db.disconnect();
		 }
		
		return d;
		
	}
	
	public static String createReviewBusinessQuery(String business_id){
		String query = "SELECT B.name, U.name, R.text, R.publish_date, R.stars, R.votes " + 
					"FROM REVIEW R, (SELECT user_id, name FROM YELP_USER) U, (SELECT business_id, name FROM BUSINESS) B " + 
					"WHERE U.user_id = R.user_id AND B.business_id = R.business_id AND R.business_id ='";
		StringBuilder queryBuilder = new StringBuilder(query);
		queryBuilder.append(business_id).append("' AND U.user_id = R.user_id");
		return queryBuilder.toString();
	}
	
	public DefaultTableModel getReviewsFromBusiness(String business_id){
		DefaultTableModel d = null;
		db = new DBConnection();
		conn = db.getConnection();
		ps = null;
		try{
			statement = "SELECT B.name, U.name as uname, R.text, R.publish_date, R.stars, R.votes " + 
					"FROM REVIEW R, (SELECT user_id, name FROM YELP_USER) U, (SELECT business_id, name FROM BUSINESS) B " + 
					"WHERE R.business_id = ? AND U.user_id = R.user_id AND B.business_id = R.business_id";
			//createReviewBusinessQuery(business_id);
			System.out.println(statement);
			ps = conn.prepareStatement(statement);
			ps.setString(1, business_id);
			ResultSet rs = ps.executeQuery();
				
			ResultSetMetaData metaData = rs.getMetaData();

			Vector<String> columnNames = new Vector<String>();
			columnNames.add("Business Name");
            columnNames.add("User Name");
            columnNames.add("Text");
            columnNames.add("Publish Date");
            columnNames.add("Stars");
            columnNames.add("Votes");
            
            String[] attr = {"name","uname", "text", "publish_date", "stars", "votes"};
	        
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) 
            {
            	Vector<Object> vector = new Vector<Object>();
            	for(String at: attr){
            		vector.add(rs.getObject(at));
            	}
            	data.add(vector);
            }
	        d = new DefaultTableModel(data, columnNames);
		    rs.close();
		    ps.close();
	         
		 } catch(SQLException e){
			e.printStackTrace();
		 } finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
			db.disconnect();
		 }
		
		return d;
	}
	
	public static String createReviewUserQuery(String user_id){
		String query = "SELECT B.name, U.name as uname, R.text, R.publish_date, R.stars, R.votes " + 
				"FROM REVIEW R, (SELECT user_id, name FROM YELP_USER) U, (SELECT business_id, name FROM BUSINESS) B " + 
				"WHERE U.user_id = R.user_id AND B.business_id = R.business_id AND R.user_id ='";;
		StringBuilder queryBuilder = new StringBuilder(query);
		queryBuilder.append(user_id).append("'");
		return queryBuilder.toString();
	}
	
	public DefaultTableModel getReviewsFromUser(String user_id){
		DefaultTableModel d = null;
		db = new DBConnection();
		conn = db.getConnection();
		ps = null;
		try{
			statement = "SELECT B.name, U.name as uname, R.text, R.publish_date, R.stars, R.votes " + 
				"FROM REVIEW R, (SELECT user_id, name FROM YELP_USER) U, (SELECT business_id, name FROM BUSINESS) B " + 
				"WHERE U.user_id = R.user_id AND B.business_id = R.business_id AND R.user_id =?";
			System.out.println(statement);
			ps = conn.prepareStatement(statement);
			ps.setString(1, user_id);
			ResultSet rs = ps.executeQuery();
				
			ResultSetMetaData metaData = rs.getMetaData();

			Vector<String> columnNames = new Vector<String>();
			columnNames.add("Business Name");
            columnNames.add("User Name");
            columnNames.add("Text");
            columnNames.add("Publish Date");
            columnNames.add("Stars");
            columnNames.add("Votes");
            
            String[] attr = {"name","uname", "text","publish_date","stars", "votes"};
	        
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) 
            {
            	Vector<Object> vector = new Vector<Object>();
            	for(String at: attr){
            		vector.add(rs.getObject(at));
            	}
            	data.add(vector);
            }
	        d = new DefaultTableModel(data, columnNames);
		    rs.close();
		    ps.close();
	         
		 } catch(SQLException e){
			e.printStackTrace();
		 } finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
			db.disconnect();
		 }
		
		return d;
	}
}


