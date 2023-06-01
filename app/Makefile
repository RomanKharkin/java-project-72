run-dist:
	./build/install/app/bin/app -h

install:
	./gradlew clean install

run-install-dist: install run-dist

report:
	./gradlew jacocoTestReport

.PHONY: build