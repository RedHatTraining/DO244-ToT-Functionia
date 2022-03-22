# Functionia

![Funtionia-8](https://user-images.githubusercontent.com/10568159/159382570-fce98c6c-eb47-43d2-a4b4-9fc7d2f49c7f.jpg)

## Prerequisites

* Quay.io account
* Quay.io repository and a robot account created in it.
* A Strimzi cluster
* Serverless Operator and Serving, Eventing, KnativeKafka enabled.
* Bitmine courier deployed:

```shell
cd bitmine-courier && ./mvnw clean package -Dquarkus.kubernetes.deploy=true
```

## Instructions

### Scene one:

Create quarkers miner clan function.

```shell
kn func create -l quarkus -t cloudevents quarkers-mining-service
```

Do the editings in the function.

Build and deploy function.

```shell
cd quarkers-mining-service
kn func deploy --build
```

Update the function service.

```shell
kn service update quarkers-mining-service --concurrency-target=1
```

Call bitmine-courier with curl with 10 mine requests.

```shell
curl -XGET http://bitmine-courier-functionia.apps-crc.testing/10
```

Checkout the pod count and logs.

```shell
stern -n functionia -c user-container quarkers-mining-service
```

### Scene two:

Delete quarkers-kafka-source.

```shell
kn source kafka delete quarkers-kafka-source
```

```shell
kn func create -l node -t cloudevents noders-mining-service
```

Do the editings in the function.
Build and deploy function.

```shell
cd noders-mining-service && kn func deploy --build
```

Update the function service.

```shell
kn service update noders-mining-service --concurrency-target=1
```

Create the kafka channel.
Create the quarkers subscription.
Create the noders subscription.

Edit bitmine-courier to produce to channel topic.

```shell
oc set env dc/bitmine-courier TOPIC=knative-messaging-kafka.functionia.kafka-channel
```

```shell
oc rollout latest dc/bitmine-courier 
```

Call bitmine-courier with curl with 50 mine requests.

```shell
curl -XGET http://bitmine-courier-functionia.apps-crc.testing/50
```

Checkout the pod count and logs.

```shell
stern -n functionia -c user-container quarkers-mining-service
```

```shell
stern -n functionia -c user-container noders-mining-service
```


### Scene three:

Delete the Kafka channel.

```shell
oc delete -f resources/2-channel-subscription/kafka-channel.yaml
```

Create the broker.

```shell
oc apply -f resources/3-broker-trigger/functionia-broker.yaml
```

Create triggers for clans.

```shell
oc apply -f resources/3-broker-trigger/quarkers-trigger.yaml
```

```shell
oc apply -f resources/3-broker-trigger/noders-trigger.yaml
```

Edit bitmine-courier to produce to channel topic.

```shell
oc set env dc/bitmine-courier TOPIC=knative-broker-functionia-kafka-broker
```

```shell
oc rollout latest dc/bitmine-courier 
```


### Scene four:

```shell
kn service update noders-mining-service --tag=noders-mining-service-00002=noders
```

```shell
kn func deploy --build
```

```shell
kn service update noders-mining-service  --tag=noders-mining-service-00003=wakanda
```

```shell
kn service update noders-mining-service  --traffic noders=80,wakanda=20
```


