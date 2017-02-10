package recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;

import dao.DatastoreProvider;
import model.Book;
import model.Rating;

public class KNNRecommendation {
	private static final int K = 5;
	private static final int NUMBER_OF_BOOKS = 10;
	private static final int QUERY_MAXIMUM_RECORDS = 100;

	private int userId;
	private HashMap<String, Integer> currentUserRatings; // isbn - rating
	private HashMap<Integer, HashMap<String, Integer>> otherUsersRatings; // userId
																			// -
																			// (isbn
																			// -
																			// rating)
	private TreeSet<Neighbour> nearestNeighbours; // priority queue
	private TreeSet<BookCandidate> candidates;
	private HashSet<String> candidatesIsbns;

	public KNNRecommendation(int userId) {
		this.userId = userId;
		this.currentUserRatings = new HashMap<String, Integer>();
		this.otherUsersRatings = new HashMap<Integer, HashMap<String,Integer>>();
		this.nearestNeighbours = new TreeSet<KNNRecommendation.Neighbour>();
		this.candidates = new TreeSet<KNNRecommendation.BookCandidate>();
		this.candidatesIsbns = new HashSet<String>();
		init();
	}

	private void init() {
		// get current user
		Query<Rating> query = DatastoreProvider.getDS().find(Rating.class);
		List<Rating> userRatings = query.field("userId").equal(this.userId).asList();
		for (Rating r : userRatings){
			currentUserRatings.put(r.getIsbn(), r.getRating());
		}
		//get other users ratings
		Query<Rating> queryOthers = DatastoreProvider.getDS().find(Rating.class);
		List<Rating> pageOne = queryOthers.field("userId").notEqual(this.userId).limit(100).asList();
		for (Rating r : pageOne){
			if (!otherUsersRatings.containsKey(r.getUserId())){
				otherUsersRatings.put(r.getUserId(), new HashMap<String, Integer>());
			}
			otherUsersRatings.get(r.getUserId()).put(r.getIsbn(), r.getRating());
		}
		int resultSize = pageOne.size();
		ObjectId lastId = pageOne.get(resultSize - 1).getId();
		while (resultSize == QUERY_MAXIMUM_RECORDS){
			Query<Rating> queryPage = DatastoreProvider.getDS().find(Rating.class);
			List<Rating> page = queryPage.field("userId").notEqual(this.userId).field("_id").greaterThan(lastId).limit(100).asList();
			for (Rating r : page){
				if (!otherUsersRatings.containsKey(r.getUserId())){
					otherUsersRatings.put(r.getUserId(), new HashMap<String, Integer>());
				}
				otherUsersRatings.get(r.getUserId()).put(r.getIsbn(), r.getRating());
			}
			resultSize = page.size();
			lastId = page.get(resultSize - 1).getId();
		}
		System.out.println(currentUserRatings.size());
		int sum = 0;
		for (HashMap<String, Integer> v: otherUsersRatings.values()){
			sum += v.size();
		}
		System.out.println(sum);
	}

	public List<Book> getRecommendations() {
		List<Book> recommendedBooks = new ArrayList<Book>();
		setNeighbours();
		setCandidates();
		Iterator<BookCandidate> iter = candidates.iterator();
		for (int i = 0; i < NUMBER_OF_BOOKS; i++) {
			if (iter.hasNext()) {
				BookCandidate candidate = iter.next();
				System.out.println(candidate.isbn);
				Query<Book> queryBook = DatastoreProvider.getDS().find(
						Book.class);
				queryBook.field("isbn").equal(candidate.isbn);
				Book book = queryBook.get();
				recommendedBooks.add(book);
			} else {
				break;
			}
		}
		return recommendedBooks;
	}

	private void setCandidates() {
		for (Neighbour n : nearestNeighbours) {
			// candidatesIsbns.addAll(otherUsersRatings.get(n.neigbourId).keySet());
			for (String isbn : otherUsersRatings.get(n.neigbourId).keySet()) {
				if (!currentUserRatings.containsKey(isbn)) {
					candidatesIsbns.add(isbn);
				}
			}
		}
		for (String isbn : candidatesIsbns) {
			double sumRating = 0;
			int occurrences = 0;
			int weight = 0;
			Iterator<Neighbour> iter = nearestNeighbours.iterator();
			while (iter.hasNext()) {
				int userId = iter.next().neigbourId;
				if (otherUsersRatings.get(userId).keySet().contains(isbn)) {
					sumRating += otherUsersRatings.get(userId).get(isbn)
							* (1 + (weight / 10));
					occurrences++;
				}
				weight++;
			}
			double avgRating = sumRating / occurrences;
			candidates.add(new BookCandidate(isbn, avgRating));
		}
	}

	private void setNeighbours() {
		for (Integer userid : otherUsersRatings.keySet()) {
			HashSet<String> commonBooks = new HashSet<String>();
			commonBooks.addAll(currentUserRatings.keySet());
			commonBooks.addAll(otherUsersRatings.get(userid).keySet());
			int[] currentUserVector = this.getVector(commonBooks,
					currentUserRatings);
			int[] otherUserVector = this.getVector(commonBooks,
					otherUsersRatings.get(userid));
			double cosineSimilarityCoef = this.calculateCosineSimilarity(
					currentUserVector, otherUserVector);
			Neighbour neighbour = new Neighbour(userid, cosineSimilarityCoef);
			// maintain k size of neighbours
			if (nearestNeighbours.size() == K) {
				if (neighbour.similarityCoef > nearestNeighbours.first().similarityCoef) {
					nearestNeighbours.remove(nearestNeighbours.first());
					nearestNeighbours.add(neighbour);
				}
			} else if (nearestNeighbours.size() < K) {
				nearestNeighbours.add(neighbour);
			}
		}
	}

	private int[] getVector(HashSet<String> commonBooks,
			HashMap<String, Integer> userRatedBooks) {
		int[] vector = new int[commonBooks.size()];
		int i = 0;
		for (String isbn : commonBooks) {
			Integer rating = userRatedBooks.get(isbn);
			if (rating != null) {
				vector[i] = rating;
			}
			i++;
		}
		return vector;
	}

	private double calculateCosineSimilarity(int[] currentUserVector,
			int[] otherUserVector) {
		int numerator = 0;
		for (int i = 0; i < currentUserVector.length; i++) {
			numerator += (currentUserVector[i] * otherUserVector[i]);
		}
		int firstDenominatorArg = 0;
		for (int i = 0; i < currentUserVector.length; i++) {
			firstDenominatorArg += Math.pow(currentUserVector[i], 2);
		}
		int secondDenominatorArg = 0;
		for (int i = 0; i < otherUserVector.length; i++) {
			secondDenominatorArg += Math.pow(otherUserVector[i], 2);
		}

		double cosineSimilarity;
		cosineSimilarity = numerator
				/ (Math.sqrt(firstDenominatorArg) * Math
						.sqrt(secondDenominatorArg));

		return cosineSimilarity;
	}

	private class Neighbour implements Comparable<Neighbour> {
		int neigbourId;
		double similarityCoef;

		public Neighbour(int neigbourId, double similarityCoef) {
			this.neigbourId = neigbourId;
			this.similarityCoef = similarityCoef;
		}

		@Override
		public int compareTo(Neighbour o) {
			if (this.similarityCoef < o.similarityCoef) {
				return -1;
			} else if (this.similarityCoef > o.similarityCoef) {
				return 1;
			}

			return 0;
		}
	}

	private class BookCandidate implements Comparable<BookCandidate> {
		String isbn;
		double avgRating;

		// int occurrences;

		public BookCandidate(String isbn, double avgRating) {
			this.isbn = isbn;
			this.avgRating = avgRating;
			// this.occurrences = 1;
		}

		// public void changeAverage(double rating){
		// avgRating = ((avgRating * occurrences) + rating) / (occurrences + 1);
		// occurrences++;
		// }

		@Override
		public int compareTo(BookCandidate o) { // decreasing order
			if (this.avgRating < o.avgRating) {
				return 1;
			} else if (this.avgRating > o.avgRating) {
				return -1;
			}

			return 0;
		}
	}
}
