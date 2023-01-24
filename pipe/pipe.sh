#!/bin/bash
# _____ _   _ _   _  ____ _____ ___ ___  _   _ ____
#|  ___| | | | \ | |/ ___|_   _|_ _/ _ \| \ | / ___|
#| |_  | | | |  \| | |     | |  | | | | |  \| \___ \
#|  _| | |_| | |\  | |___  | |  | | |_| | |\  |___) |
#|_|    \___/|_| \_|\____| |_| |___\___/|_| \_|____/
#
function TEST_BIN() {
  if [ $(which $1 | wc -l | awk '{print $1} ') -eq 0 ]
    then
      echo $1 not found
      break
    else
      if [ $(file "$(which $1)" 1>&1 >/dev/null;echo $?) -ne 0 ]
      then
        echo $1 not found
        break
      else
        echo $1 OK
      fi
    fi
}
function RUN() {
  export RC=3
  while true
  do
    date
    echo $1
    time $1
    if [ $? -ne 0 ]
    then
      echo FAIL $?
      echo FAIL $1
      if [ "$2" != "ignore" ] || [ "$2" != "retry_ignore" ]
      then
        echo Continue
        export RC=0
        if [ "$2" == "retry" ] || [ "$2" != "retry_ignore" ]
        then
          if [ $RC -eq 3 ]
          then
            export RC=2
          else
            export RC=1
            exit 1
          fi
        fi
      else
        exit 1
      fi
    else
      echo SUCCESS $1
      export RC=0
    fi
    if [ $RC -eq 0 ]
    then
      break
    fi
  done
}
# _____ _____ ____ _____
#|_   _| ____/ ___|_   _|
#  | | |  _| \___ \ | |
#  | | | |___ ___) || |
#  |_| |_____|____/ |_|
#
for BIN in {docker,kubectl,kind,mvn,keytool,openssl}
do
  TEST_BIN $BIN
done
#__     ___    ____  ____
#\ \   / / \  |  _ \/ ___|
# \ \ / / _ \ | |_) \___ \
#  \ V / ___ \|  _ < ___) |
#   \_/_/   \_\_| \_\____/
#
export TAG_COUNT=$(cat pipe/build.tag.count)
expr $TAG_COUNT + 1 > pipe/build.tag.count
export BUILD_TAG=1.$TAG_COUNT
export BUILD_REGISTRY=localhost:5000
export BUILD_DST_IMAGE=awh-sb01
export BUILD_SRC_IMAGE=mcr.microsoft.com/openjdk/jdk:17-ubuntu
export KUBECONFIG=$HOME/.kube/configs/kind

# ____  ____  _____ ____   _    ____  _____
#|  _ \|  _ \| ____|  _ \ / \  |  _ \| ____|
#| |_) | |_) |  _| | |_) / _ \ | |_) |  _|
#|  __/|  _ <| |___|  __/ ___ \|  _ <| |___
#|_|   |_| \_\_____|_| /_/   \_\_| \_\_____|
#
cat pipe/template-awh-deploy.yml | sed -e 's|TEMPLATE_IMAGE|'$BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG'|g' > awh-deploy.yml
cd $WORKDIR
# ____  _   _ _   _ ____
#|  _ \| | | | \ | / ___|
#| |_) | | | |  \| \___ \
#|  _ <| |_| | |\  |___) |
#|_| \_\\___/|_| \_|____/
#

RUN "mvn compile jib:build -Dmaven.wagon.http.ssl.insecure=true package"
RUN "docker pull $BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG"
RUN "kind load docker-image $BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG"
RUN "docker image ls"
RUN "kubectl get nodes"
RUN "kubectl delete ns awh" ignore
RUN "kubectl create ns awh"
RUN "kubectl -n awh apply -f awh-deploy.yml"
RUN "kubectl -n awh get all"


#keytool -genkeypair -alias baeldung -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore baeldung.p12 -validity 3650
#keytool -genkeypair -alias baeldung -keyalg RSA -keysize 2048 -keystore baeldung.jks -validity 3650
#keytool -importkeystore -srckeystore baeldung.jks -destkeystore baeldung.p12 -deststoretype pkcs12
#
#openssl genrsa -des3 -out myCA.key 2048
#
#openssl req -x509 -new -nodes -key myCA.key -sha256 -days 1825 -out myCA.pem
#
##CA
#openssl genrsa -out rootCA.key 2048
openssl genrsa -des3 -passout pass:abc -out rootCA.key 2048
openssl req -x509 -new -nodes -key rootCA.key -passin pass:abc -sha256 -days 3650 -out rootCA.pem -subj "/C=BR/ST=Sao Paulo/L=Sao Paulo/O=br.com.clusterlab/OU=Clusterlab/CN=shm/emailAddress=devops@clusterlab.com.br"
