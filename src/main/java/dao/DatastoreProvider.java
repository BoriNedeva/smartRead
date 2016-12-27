package dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import dao.Constants;

import com.mongodb.MongoClient;

public class DatastoreProvider {

	private final Morphia morphia;
    private Datastore ds;
	
	public DatastoreProvider(Morphia morphia) {
        this.morphia = morphia;
        initializeInstanceIfNecessary();
    }

    private void initializeInstanceIfNecessary() {
        if (ds != null) {
            return;
        }
        MongoClient mongoClient = new MongoClient(Constants.HOST, Constants.PORT);
        ds = morphia.createDatastore(mongoClient, Constants.DB_NAME);
        ds.ensureIndexes();
    }
    
    public Datastore getDS() {
        return ds;
    }
}
