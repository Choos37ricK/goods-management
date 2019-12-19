import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

        MongoCollection<Document> goodsCollection = mongoDatabase.getCollection("goods");
        MongoCollection<Document> shopsCollection = mongoDatabase.getCollection("shops");

        //goodsCollection.drop();
        //shopsCollection.drop();
        Thread.sleep(1000);

        Scanner scanner = new Scanner(System.in);

        while(true) {
            printCommands();
            System.out.print(">>> ");
            String commandLine = scanner.nextLine();

            if (commandLine.isEmpty()) {
                System.out.println("Введите команду!");
                continue;
            }

            String[] commandParts = commandLine.split("\\s+");
            String command = commandParts[0].toLowerCase();

            switch (command) {
                case "статистика_товаров":
                    System.out.println("Общее количество товаров: " + goodsCollection.countDocuments());
                    System.out.println("Средняя цена товара: " + goodsCollection.;
                    break;
                case "добавить_магазин":
                    Document shop = new Document().append("name", commandParts[1]);
                    shopsCollection.insertOne(shop);
                    System.out.println("Магазин добавлен:\n" + shopsCollection.find(shop).iterator().next().toJson());
                    break;
                case "добавить_товар":
                    Document product = new Document()
                            .append("name", commandParts[1])
                            .append("price", commandParts[2]);
                    goodsCollection.insertOne(product);
                    System.out.println("Товар добавлен:\n" + goodsCollection.find(product).iterator().next().toJson());
                    break;
                case "выставить_товар":
                    Document product2Shop = goodsCollection.find(new BasicDBObject("name", commandParts[1])).iterator().next();
                    shopsCollection.updateOne(
                            Filters.eq("name", commandParts[2]),
                            new Document("$push", new Document("goods", product2Shop)));
                    System.out.println("Товар выставлен:\n" + shopsCollection.find(Filters.eq("name", commandParts[2])).iterator().next().toJson());
                    break;
                default:
                    System.out.println("\nНеверный формат команды!");
                    break;
            }
        }

    }

    private static void printCommands() {
        System.out.println();
        System.out.println("1. Чтобы добавить магазин введите: добавить_магазин Название");
        System.out.println("2. Чтобы добавить магазин введите: добавить_товар Название Количество");
        System.out.println("3. Чтобы добавить товар в магазин введите: выставить_товар НазваниеТовара НазваниеМагазина");
        System.out.println("4. Чтобы вывести информацию о товарах во всех магазинах введите: статистика_товаров");
    }
}
