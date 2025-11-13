FROM ubuntu:latest
LABEL authors="Microsoft"

ENTRYPOINT ["top", "-b"]