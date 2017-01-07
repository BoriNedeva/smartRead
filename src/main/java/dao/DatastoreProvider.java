package dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import dao.Constants;

import com.mongodb.MongoClient;

public class DatastoreProvider {

	private final Morphia morphia;
    private Datastore ds;
    
	private static DatastoreProvider instance;
    
	private DatastoreProvider() {
        this.morphia = new MorphiaProvider().get();
        MongoClient mongoClient = new MongoClient(Constants.HOST, Constants.PORT);
        ds = morphia.createDatastore(mongoClient, Constants.DB_NAME);
        ds.ensureIndexes();
    }

    /*private void initializeInstanceIfNecessary() {
        if (ds != null) {
            return;
        }
        MongoClient mongoClient = new MongoClient(Constants.HOST, Constants.PORT);
        ds = morphia.createDatastore(mongoClient, Constants.DB_NAME);
        ds.ensureIndexes();
    }*/
    
    public static Datastore getDS() {
    	if (instance == null){
    		instance = new DatastoreProvider();
    	}
        return instance.ds;
    }
}
