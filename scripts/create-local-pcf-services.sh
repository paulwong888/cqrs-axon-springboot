#!/usr/bin/env bash

cf create-service p-mysql 1gb mysql
cf create-service p-rabbitmq standard rabbit