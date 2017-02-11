package recommendation;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.*;
import java.io.*;

import javax.rmi.CORBA.Util;
import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.soap.AddressingFeature.Responses;

import com.google.gson.Gson;


import model.BXUser;
import model.Rating;
import model.Book;

import util.ResponseMessages;
import util.Utils;


import dao.DatastoreProvider;
import dao.MorphiaProvider;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import dto.LoginUser;
import dto.RegistrationUser;
import dto.Token;


//@Path("algorithm")
public class DBscan { 
	
	private static List<BXUser> pointList = new ArrayList<BXUser>();
	private static LinkedList<BXUser> queueList = new LinkedList<BXUser>();
	
	public final static int e = 3; //espilon distance
	
	public final static int minp = 4; //minimum points
	
	public List<BXUser> pointsList = new ArrayList<BXUser>();
	
	public List<List<BXUser>> resultList = new ArrayList<List<BXUser>>();
	
//	@POST
//	@Path("dbscan")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response main(LoginUser user) {
//		DBscan c = new DBscan();
//		Query<BXUser> query = DatastoreProvider.getDS().createQuery(BXUser.class)
//				.disableValidation().filter("username = ", user.getUsername());
//		try {
//			c.applyDbscan();
//			BXUser saved = (BXUser) query.get();
//			int age = saved.getAge();
//			int cluster = c.assignCluster(resultList, age);
//			//c.getBooks(cluster);
//			//Gson gson = new Gson();
//			//return Utils.responseBuilder(Response.Status.OK,
//			//	new Token(c.getBooks(cluster)));
//			//return Response.status(Response.Status.OK).entity(gson.toJson(c.getBooks(cluster))).build();
//			return Utils.responseBuilder(Response.Status.OK, c.getBooks(cluster));
//		}catch (Exception e){
//			e.printStackTrace();
//			//return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//			return Utils.responseBuilder(Response.Status.INTERNAL_SERVER_ERROR, null);
//		}
//	}

	public void applyDbscan() throws IOException {
		pointsList = getPointsList();
		for(int index = 0; index < pointsList.size(); ++index) {
			List<BXUser> tmpLst=new ArrayList<BXUser>();
			BXUser p = pointsList.get(index);
			if(p.isClassed())
				continue;
			tmpLst = isKeyPoint(pointsList, p, e, minp);
			if(tmpLst != null) {
				resultList.add(tmpLst);
			}
		}
		int length = resultList.size();
		for(int i = 0; i < length; ++i) {
			for(int j = 0; j < length; ++j)
			{
				if(i != j) {
					if(mergeList(resultList.get(i), resultList.get(j))) {
						resultList.get(j).clear();
					}
				}

			}
		}
	}

	//calculate the distance between two points
	public double getDistance(BXUser p,BXUser q) {
		double dx = p.getAge() - q.getAge();
		double distance = Math.sqrt(dx*dx);
		return distance;
	}
	
	public double getDistance2(BXUser p,int q) {
		double dx = p.getAge() - q;
		double distance = Math.sqrt(dx*dx);
		return distance;
	}

	//check wheather p is a core point or not
	public List<BXUser> isKeyPoint(List<BXUser> lst,BXUser p,int e,int minp) {
		int count = 0;
		List<BXUser> tmpLst = new ArrayList<BXUser>();
		for(Iterator<BXUser> it = lst.iterator(); it.hasNext();) {
			BXUser q = it.next();
			if(getDistance(p,q) <= e) {
				++count;
				if(!tmpLst.contains(q)) {
					tmpLst.add(q);
				}
			}
		}
		if(count >= minp) {
			p.setKey(true);
			return tmpLst;
		}
		return null;
	}

	//Get the sample points in the text collection
	public List<BXUser> getPointsList() throws IOException {
		Query<BXUser> query = DatastoreProvider.getDS().find(BXUser.class);
		List<BXUser> lst = new ArrayList<BXUser>();
		lst = query.asList();
//		String txtPath = "C:\\smartRead-master\\src\\age.txt";
//		BufferedReader br = new BufferedReader(new FileReader(txtPath));
//		String str = "";
//		while((str = br.readLine()) != null && str != "") {
//				lst.add(new BXUser(str));
//		}
//		br.close();
		return lst;
		/*int i = 0;
		while(lst.size() < 20) {
			int age = DatastoreProvider.getDS().createQuery(BXUser.class).project("age",true).asList().get(i).getAge();
			lst.add(age);
			i++;
		}*/
	}

	//Combined, the two linked list, the premise is that in the core point in included in a
	public boolean mergeList(List<BXUser> a,List<BXUser> b) {
		boolean merge = false;
		if(a == null || b == null) {
			return false;
		}
		for(int index = 0;index < b.size(); ++index) {
			BXUser p = b.get(index);
			if(p.isKey() && a.contains(p)) {
				merge = true;
				break;
			}
		}
		if(merge) {
			for(int index = 0; index < b.size(); ++index) {
				if(!a.contains(b.get(index))) {
					a.add(b.get(index));
				}
			}
		}
		return merge;
	}

	
	//assign user to a cluster 
	public int assignCluster (int p) {
		double min = 100;
		int index = 1;
		int ind = 1;
		List userIds = new ArrayList<>();
		for(Iterator<List<BXUser>> it = resultList.iterator();it.hasNext();) {
			List<BXUser> lst = it.next();
			if(lst.isEmpty()) {
				continue;
			}
			for(Iterator<BXUser> it1 = lst.iterator();it1.hasNext();) {
				BXUser q = it1.next();
				if (getDistance2 (q,p) < min) {
					min = getDistance2(q,p);
					ind = index;
					userIds.add(q);
				}
			}
			index++;
		}
        System.out.println(userIds.toArray());
        printt(userIds);
		return ind;
	}

	public void printt (List n) {
		for (int i = 0; i < n.size(); i++) {
			System.out.println(n.get(i));
		}
	}
		
	//recommendate books by clusters
	public List<Book> getBooks(int cluster) {
		if (cluster == 1) {
		List<Rating> query = new ArrayList<Rating>();
		query = DatastoreProvider.getDS().createQuery(Rating.class)
			.filter("userId =  ", 277427).filter("rating = ", 10).limit(25).asList();
		
		List<Book> books = new ArrayList<Book>(30);
		int i = 0;
		while(books.size() < 10) {
			String str = query.get(i).getIsbn();
			books.add(DatastoreProvider.getDS().createQuery(Book.class).field("isbn").equal(str).get());
			i++;
		}
		return books;
		} 
		
		else {
			List<Rating> query = new ArrayList<Rating>();
			query = DatastoreProvider.getDS().createQuery(Rating.class)
				.filter("userId =  ", 278137).filter("rating = ", 9).limit(10).asList();
			
			List<Book> books = new ArrayList<Book>(30);
			int i = 0;
			while(books.size() < 10) {
				String str = query.get(i).getIsbn();
				books.add(DatastoreProvider.getDS().createQuery(Book.class).field("isbn").equal(str).get());
				i++;
			}
			return books;
		}
	}
}