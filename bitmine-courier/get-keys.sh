#!/usr/bin/env bash

rm truststore.jks

oc extract secret/my-cluster-cluster-ca-cert --keys=ca.crt --to=- > ca.crt

echo "yes" | keytool -import -trustcacerts -file ca.crt -keystore truststore.jks -storepass test1234 -alias CARoot

rm ca.crt