package springaws.handlingformsubmission;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Component("DynamoDBEnhanced")
public class DynamoDBEnhanced {

    public void injectDynamoItem(Greeting greeting) {
        Region region = Region.EU_CENTRAL_1;
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        try {
            DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();

            DynamoDbTable<GreetingItems> mappedTable = dynamoDbEnhancedClient
                    .table("Greeting", TableSchema.fromBean(GreetingItems.class));
            GreetingItems greetingItems = new GreetingItems();
            greetingItems.setId(greeting.getId());
            greetingItems.setMessage(greeting.getBody());
            greetingItems.setTitle(greeting.getTitle());
            greetingItems.setName(greeting.getName());

            PutItemEnhancedRequest<GreetingItems> greetingItemsBuilder = PutItemEnhancedRequest
                    .builder(GreetingItems.class)
                    .item(greetingItems)
                    .build();
            mappedTable.putItem(greetingItemsBuilder);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
