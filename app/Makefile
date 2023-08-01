run-dist:
	./build/install/app/bin/app -h

install:
	./gradlew clean install

run-install-dist: install run-dist

run:
	APP_ENV=development ./gradlew run

run-dist:
	APP_ENV=production ./build/install/app/bin/app

stop:
	./gradlew stop

report:
	./gradlew jacocoTestReport

generate-migrations:
	./gradlew generateMigrations

.PHONY: build