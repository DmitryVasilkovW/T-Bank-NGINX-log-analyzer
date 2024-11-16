build:
	./mvnw clean package

run:
	java -jar target/java-1.0.0.jar $(ARGS)


