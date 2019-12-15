import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);

        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

        MongoCollection<Document> goodsCollection = mongoDatabase.getCollection("goods");
        MongoCollection<Document> shopsCollection = mongoDatabase.getCollection("shops");

        goodsCollection.drop();
        shopsCollection.drop();

        while(true) {
            printCommands();
            System.out.print(">>> ");
            Scanner scanner = new Scanner(System.in);

            String commandLine = scanner.toString();
            if (commandLine.isEmpty()) {
                System.out.println("Введите команду!");
                continue;
            }

            String[] commandParts = commandLine.split("\\s+");
            String command = commandParts[0].toLowerCase();
            if (commandParts.length != 3) {
                if (!command.equals("статистика_товаров")) {
                    System.out.println("\nНеверный формат команды!\n");
                    continue;
                }
            }

            switch (command) {
                case "статистика_товаров":
                    break;
                case "добавить_магазин":
                    Document shop = new Document().append("name", commandParts[1]);
                    shopsCollection.insertOne(shop);
                    break;
                case "добавить_товар":
                    Document goods = new Document()
                            .append("name", commandParts[1])
                            .append("price", commandParts[2]);
                    goodsCollection.insertOne(goods);
                    break;
                case "выставить_товар":
                    goodsCollection.find(); //нужно же сперва из коллекции товаров получить товар с его названием и ценой?
                    //и затем вставить его в список товаров магазина?
                    //но find() возвращает Iterable, а мне нужен один объект для вставки в запрос ниже
                    //и да, разве запрос на добавление в список товаров будет настолько сложен и запутан? в консоли это прилично выглядит, а тут...
                    shopsCollection.updateOne(Filters.eq("name", commandParts[2], new Document("$push", new Document("goods", commandParts[1]}}});
                    break;

            }
        }
    }

    private static void printCommands() {
        System.out.println("1. Чтобы добавить магазин введите: добавить_магазин Название");
        System.out.println("2. Чтобы добавить магазин введите: добавить_товар Название Количество");
        System.out.println("3. Чтобы добавить товар в магазин введите: выставить_товар НазваниеТовара НазваниеМагазина");
        System.out.println("4. Чтобы вывести информацию о товарах во всех магазинах введите: статистика_товаров");
    }
}
