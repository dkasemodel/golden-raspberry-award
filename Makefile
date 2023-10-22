build:
	mvn clean install

test:
	mvn clean test

start:
	java -jar $(EXTRA_PARAMS) ./target/*.jar

build-start: build start

.DEFAULT_GOAL := build-start
