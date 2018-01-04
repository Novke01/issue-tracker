FROM openjdk:8u151-jdk-alpine as builder

ENV SBT_VERSION 1.0.4
ENV SBT_HOME /usr/local/sbt

COPY ./target/universal/issue-tracker-1.0-SNAPSHOT.zip .
RUN unzip issue-tracker-1.0-SNAPSHOT.zip

FROM openjdk:8u151-jdk-alpine
RUN apk update && apk add bash
COPY --from=builder /issue-tracker-1.0-SNAPSHOT /issue-tracker-1.0-SNAPSHOT
RUN chmod +x /issue-tracker-1.0-SNAPSHOT/bin/issue-tracker

ENTRYPOINT ["./issue-tracker-1.0-SNAPSHOT/bin/issue-tracker"]
CMD ["-Dplay.http.secret.key=issuetracker"]
EXPOSE 9000