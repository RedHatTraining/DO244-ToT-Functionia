# Functionia

![Funtionia-8](https://user-images.githubusercontent.com/10568159/159382570-fce98c6c-eb47-43d2-a4b4-9fc7d2f49c7f.jpg)

## Prerequisites

* Quay.io account
* Quay.io repository and a robot account created in it.
* `stern` tool installed.
* A Strimzi cluster
* Serverless Operator and Serving, Eventing, KnativeKafka enabled.
* Bitmine courier deployed:

```shell
cd bitmine-courier && ./mvnw clean package -Dquarkus.kubernetes.deploy=true
```

## Instructions

### Scene one:

![Screen Shot 2022-03-24 at 15 53 36](https://user-images.githubusercontent.com/10568159/159920605-07514b71-aa35-415e-860d-0aea76309183.png)

Create a project namespace.

```shell
oc new-project functionia
```

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

![Screen Shot 2022-03-24 at 15 53 47](https://user-images.githubusercontent.com/10568159/159920643-110ad8f4-a75b-4749-be47-f1e0afda62f1.png)

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

![Screen Shot 2022-03-24 at 15 53 56](https://user-images.githubusercontent.com/10568159/159920699-e114137b-404b-40bb-a5bf-069cbf1da73c.png)

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

![Screen Shot 2022-03-24 at 15 53 10](https://user-images.githubusercontent.com/10568159/159920744-51a1cf1d-c0aa-43f7-bcbf-7c1a443338c8.png)

Tag Noders service as `noders`.

```shell
kn service update noders-mining-service --tag=noders-mining-service-00002=noders
```

Edit the NodeJS function and build/deploy it.

```shell
kn func deploy --build
```

Tag the new version as `wakanda`.

```shell
kn service update noders-mining-service  --tag=noders-mining-service-00003=wakanda
```

Split the traffic as requested.

```shell
kn service update noders-mining-service  --traffic noders=80,wakanda=20
```


