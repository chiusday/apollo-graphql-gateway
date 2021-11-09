const { ApolloServer } = require('apollo-server');
const { ApolloGateway } = require('@apollo/gateway');

const gateway = new ApolloGateway({
  serviceList: [
    {name: "account", url: "http://localhost:8084/graphql"},
    {name: "customer", url: "http://localhost:8083/graphql"}
  ]
});
const server = new ApolloServer({ gateway, subscriptions: false });

server.listen(8090).then(({ url }) => {
  console.log(`Gateway API running at ${url}`);
});
