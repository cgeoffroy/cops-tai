all: compile

test:
	mvn integration-test

install:
	mvn package

run:
	java -jar target/cops-tai.jar

build:
	mvn compile

PHONY: test install run build