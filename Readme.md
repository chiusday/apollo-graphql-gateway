#### Steps to supplement graphql-java projects with Netflix DGS is detailed [here](customer-dgs/Readme.md)

## Steps on how to enable Apollo Gateway to add type(s) from one GraphQL microserviceto another.
   _Note: this example will enable account-dgs to extend Customer type to add list of accounts of a given customer represented 
   by the field "accountsForCustomer"_

1. Define the parent type (object) as "entity" by specifying the @key field(s). 
   This is the field(s) that links this type (object) to another type in another service (Customer.id = Account.customerId) 
   Sample below from [customer-dgs](customer-dgs/src/main/resources/schema/schema.graphqls). **That's it! No other change required
   in the parent type (object)**.
   ```
    type Customer @key(fields: "id") {
        id: ID!
        ...
    }
   ```
2. Apollo Gateway can now look for "references" of Customer type in other microservices and supplement the fields provided 
   by the "referencing" microservices, in this case account-dgs. 
   The Customer type can now be "referenced" in [account-dgs](account-dgs/src/main/resources/schema/account.graphqls)
   * @extends - Indicates that this is a referenced type.
     When a subgraph extends an entity, the entity's originating subgraph is not aware of the added fields. 
     Only the extending subgraph (along with the gateway) knows about these fields.
   * @external - This is the field whose value will come from the originating subgraph that will be passed by Apollo Gateway to this service 
   ```
   # This is a stub of Customer type in customer-dgs service
   type Customer @key(fields: "id") @extends {
      id: ID @external
      accountsForCustomer: [Account]
   }
   ```
3. Now that the child schema [account-dgs](account-dgs/src/main/resources/schema/account.graphqls) 
   has a reference of Customer entity, code generators ([DGS Code Generator](https://netflix.github.io/dgs/generating-code-from-schema) 
   for Gradle or [community ported](https://github.com/deweyjose/graphqlcodegen) for Maven) can be used to generate boilerplate codes.
   This example used [community ported](https://github.com/deweyjose/graphqlcodegen).  
   Steps to use the code generator for maven
   * add the following build plugins to account-dgs
   ```
        <plugins>
            <plugin>
                <groupId>io.github.deweyjose</groupId>
                <artifactId>graphqlcodegen-maven-plugin</artifactId>
                <version>1.12</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <schemaPaths>
                        <param>src/main/resources/schema/query.graphqls</param>
                        <param>src/main/resources/schema/account.graphqls</param>
                    </schemaPaths>
                    <packageName>edu.jon.graphql.account.generated</packageName>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <source>${project.build.directory}/generated-sources</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
   ```
4. Complie account-dgs project and the stubs and constants will be generated.
5. Create the method that will add the Customer's ID into the context (DgsDataFetchingEnvironment)
   * Annotate the function with @DgsEntityFetcher. This will tell the framework which method to invoke 
     when the Customer data is passed by the Gateway. The "name" property is the entity name defined in 
     schema [account-dgs](account-dgs/src/main/resources/schema/account.graphqls)
   * The Map parameter will contain the Customer field(s) specified as @external in 
     [account-dgs](account-dgs/src/main/resources/schema/account.graphqls)
   * This method returns the instance of the extended (@extends) type, which in this case - Customer. Set the Customer.id to the value 
     retrieved from the Map input parameter so that it can be used to identify related accounts.
   ```
    @DgsEntityFetcher(name = DgsConstants.CUSTOMER.TYPE_NAME)
    public Customer getCustomer(Map<String, Object> values) {
        return new Customer((String)values.get("id"), null);
    }
   ```  
6. Create a method that will fetch the list of Accounts related to the given Customer.id
   * Annotate the method with @DgsData 
      * parentType - extended entity Customer    
      * field - the field specified in entity Customer in [account-dgs](account-dgs/src/main/resources/schema/account.graphqls)
      that is supposed to supplement the originating Customer type
   * dfe.getSource() returns the instance of the parentType
   ```
    @DgsData(parentType = "Customer", field = "accountsForCustomer")
    public List<Account> accountsForCustomer(DgsDataFetchingEnvironment dfe) {
        Customer customer = dfe.getSource();
        return accountDataService.getAccountsFor(customer.getId());
    }
   ```
## That's it! 
* Run gateway 
* customer-dgs 
* account-dgs    
_Now, GraphQL requests to Customer can include accountsForCustomer field and will return related accounts_

This is great, however, if accounts have to be fetched for multiple customers it will make N calls to account
microservice (ex: [account-dgs.schema.graphqls](account-dgs/src/main/resources/schema/account.graphqls).Customer.accountsForCustomersSync). 
This is known as [The N+1 Problem](https://medium.com/the-marcy-lab-school/what-is-the-n-1-problem-in-graphql-dd4921cb3c1a).  

## Implementing BatchLoaders to solve the N+1 Problem
* add a new field in [account-dgs.schema.graphqls](account-dgs/src/main/resources/schema/account.graphqls).Customer that corresponds to
a DGS BatchLoader implementation - _accountsForCustomersBatched_  
* The rest of the steps are explained [here](customer-dgs/Readme.md#Batch-Loading-to-solve-the-N+1-problem)  
