VERSION=1.0.0
APP_NAME=nginx-log-analyzer
JAR_FILE=target/$(APP_NAME)-$(VERSION).jar
DIST_DIR=dist

build:
	./mvnw clean package

run:
	@java -jar target/java-1.0.0.jar $(ARGS)

prepare-dist: build
	@echo "Preparing distribution files..."
	mkdir -p $(DIST_DIR)
	cp $(JAR_FILE) $(DIST_DIR)/$(APP_NAME)-$(VERSION).jar
	echo "#!/bin/bash\njava -jar $(APP_NAME)-$(VERSION).jar \"\$$@\"" > $(DIST_DIR)/run.sh
	echo "@echo off\njava -jar $(APP_NAME)-$(VERSION).jar %*" > $(DIST_DIR)/run.bat
	chmod +x $(DIST_DIR)/run.sh

release-zip: prepare-dist
	zip -r $(DIST_DIR)/$(APP_NAME)-$(VERSION).zip -j $(DIST_DIR)/*

release-tar: prepare-dist
	tar -czvf $(DIST_DIR)/$(APP_NAME)-$(VERSION).tar.gz -C $(DIST_DIR) .

clean:
	rm -rf $(DIST_DIR)
