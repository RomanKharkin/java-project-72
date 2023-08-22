FROM gradle:7.4.0-jdk20

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app