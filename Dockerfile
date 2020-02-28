# Ubuntu 18.04 with JDK 11
# Build image with:  docker build -t csci652:latest .

FROM ubuntu:18.04
MAINTAINER Peizhao Hu, http://cs.rit.edu/~ph

# install all dependencies
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y  software-properties-common && \
    apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    apt-get install -y net-tools iputils-ping maven gradle nmap wget git vim build-essential && \
    apt-get clean

# create a new directory as the working directory
RUN mkdir /csci652


# copy files from the directory of the Dockerfile on your computer to this docker build environment.
COPY project2 /csci652/project2
COPY pom.xml /csci652/
COPY README.md /csci652/
COPY LICENSE /csci652/

# You will need add COPY commands to copy your project source code into the docker build environment.
# e.g., COPY project1 /csci652/project1

# setup working directory in the container
WORKDIR /csci652

# go into the working directory and build java package using maven
RUN cd /csci652 && mvn package && cd /csci652/project2