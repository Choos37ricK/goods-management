import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

        MongoCollection<Document> goodsCollection = mongoDatabase.getCollection("goods");
        MongoCollection<Document> shopsCollection = mongoDatabase.getCollection("shops");

        goodsCollection.drop();
        shopsCollection.drop();


    }
}
