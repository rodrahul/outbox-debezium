
[Reliable Microservices Data Exchange With the Outbox Pattern](https://debezium.io/blog/2019/02/19/reliable-microservices-data-exchange-with-the-outbox-pattern/)

The overall solution looks like so:

![Service Overview](outbox-overview.png)


## Environment

Setup the necessary environment variables

```console
$ export DEBEZIUM_VERSION=2.4.2.Final
$ export DEBEZIUM_CONNECTOR_VERSION=2.4.2.Final
```

The `DEBEZIUM_VERSION` specifies which version of Debezium images should be used.
The `DEBEZIUM_CONNECTOR_VERSION` specifies which version of Debezium connector artifacts should be used.

## Configure the Debezium connector
Register the connector that to stream outbox changes from the order service:
Once you've all the docker components up and running
run the following command command to register 

```
$ http PUT http://localhost:8083/connectors/outbox-connector/config < register-postgres.json
HTTP/1.1 201 Created
```


Changes needed from the tutorial
* Update strimzi-connect env config to value.converter=org.apache.kafka.connect.storage.StringConverter, as the kafka values were string escaped
* Added postresql and conduktor-platform for kafka UI