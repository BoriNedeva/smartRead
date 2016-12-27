package dao;

import org.mongodb.morphia.Morphia;

public class MorphiaProvider {

	public Morphia get() {
        Morphia morphia = new Morphia();
        morphia.mapPackage("model");
        return morphia;
    }
}
