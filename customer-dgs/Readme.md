## Since Netflix DGS framework is built on top of graphql-java, it is easy to supplement graphql-java projects with DGS.

### 1. Add DGS depenedencies and dependency management
```
    <dependency>
        <groupId>com.netflix.graphql.dgs</groupId>
        <artifactId>graphql-dgs-spring-boot-starter</artifactId>
    </dependency>
```

and

```
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.netflix.graphql.dgs</groupId>
                <artifactId>graphql-dgs-platform-dependencies</artifactId>
                <version>4.1.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```   

### 2. graphql-java's resolver is Data Fetcher in DGS. So, copy the resolver class and:
   - Don't implement any interface. (I also renamed the class from *Resolver to *DataFetcher)
   - Annotate the data fetcher class with @DgsComponent
   ```
    @Slf4j
    @DgsComponent
    @RequiredArgsConstructor
    public class CustomerDataFetcher {
        private final CustomerDataService customerDataService;
   ```   
   - Annotate the method corresponding with the Query entry in *.graphqls with @DgsQuery
   ```
    @DgsQuery
    public List<Customer> customers() {
        return customerDataService.getAllCustomers();
    }
   ```
   - Annotate parameters that is supposed to be supplied by the query with @InputArguement
   ```
    @DgsQuery
    public Customer customer(@InputArgument String id) {
        return customerDataService.getCustomer(id);
    }
   ```

### 3. move the graphql schema files (*.graphqls) to "resources/schema" directory. That's it! The project can now take full advantage of DGS features.  
  
- - -  

## DataFetcher (Resolver) for Child Objects
Schema types with fields that are other types (ex: Customer -> Contact) has a parent child relationship
   ```
    type Customer {
        id: ID!
        firstName: String!
        lastName: String!
        contacts: [Contact]
    }
    
    type Contact {
        id: ID!
        customerId: ID!
        contactType: ContactType!
        value: String!
    }
   ```
These are the changes required:
### 1. Annotate the child DataFetcher class with @DgsComponent
   ```
    @DgsComponent
    @RequiredArgsConstructor
    public class ContactDataFetcher {
        private final ContactDataService contactDataService;
   ```

### 2. Annotate the method with @DgsData with the following attributes:
- parentType - The type in the *.graphqls schema that has this type as it's child field. In the example below, it is "Customer"
- field - the field name of this child field in the parentType. In the exmaple below, it is "contacts". If this is missing, the method name will be the field
   ```
    @DgsData(parentType = "Customer", field = "contacts")
    public List<Contact> contacts(DgsDataFetchingEnvironment dfe) {
        Customer customer = dfe.getSource();
        return contactDataService.contacts(customer.getId());
    }
   ```

### 3. Pass the DsgDataFetchingEnvironment
   DsgDataFetchingEnvironment is provided by the framework. "getSource()" will return the instance of the parentType.
   Use it to get the relevant data from the parent type necessary to fetch the child type data
   ```
    public List<Contact> contacts(DgsDataFetchingEnvironment dfe) {
        Customer customer = dfe.getSource();
        return contactDataService.contacts(customer.getId());
    }
   ```

## Batch Loading to solve the N+1 problem
Use DataLoaders to collect the keys from a parent and fetch the list of child object related to the set of parent keys.
This eliminates the need to call the child fetch method N times, instead call once.
There are a number of ways to implement batch loading in DGS. In this sample, MappedBatchLoader is used because it easily handles cases where child object is not found for a parent key(s)

### Steps to implement Batch Loading in DGS

- Implement a MappedBatchLoader interface
   ```
    public class ContractsDataLoader implements MappedBatchLoader<String, List<Contact>> {
   ```
- Annotate the class with @DgsDataLoader with name parameter
   ```
    @DgsDataLoader(name = "CONTACTS_DATALOADER")
    public class ContractsDataLoader implements MappedBatchLoader<String, List<Contact>> {
   ```
- Implement the "load" method to fetch the collection of child objects based on the given set of keys from parent object
   ```
    @Override
    public CompletionStage<Map<String, List<Contact>>> load(Set<String> customerIds) {
        return CompletableFuture.supplyAsync(() ->
                contactDataService.contactsFor(customerIds)
            );
    }
   ```
- Use the MappedDataLoader in the DataFetcher (ContactDataFetcher). "getSource()" returns the parentType instance
   ```
    @DgsData(parentType = "Customer", field = "contactsBatched")
    public CompletableFuture<List<Contact>> batchedContacts(DgsDataFetchingEnvironment dfe) {
        DataLoader contactsDataLoader = dfe.getDataLoader(ContactsDataLoader.class);
        Customer customer = dfe.getSource();
        return contactsDataLoader.load(customer.getId().toString());
    }
   ```
