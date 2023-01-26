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
function CERTIFICATE() {
  export SSL_CA_KEY=rootCA.key
  export SSL_CA_CERT=rootCA.crt
  export SSL_DEVICE_KEY=device.key
  export SSL_REQUEST_KEY=device.csr
  export SSL_DEVICE_CERT=device.crt
  export SSL_KS_P12=ks.p12
  export SSL_KS_JKS=ks.jks
  export TMPBASEDIR=tmp
  export BASEDIR=extra


  RUN "rm -f $TMPBASEDIR/*" ignore
  RUN "rm -f $BASEDIR/*" ignore

  RUN "mkdir -p $BASEDIR" ignore
  RUN "mkdir -p $TMPBASEDIR" ignore

  RUN "openssl genrsa -des3 -passout pass:$SSL_PASS -out $TMPBASEDIR/$SSL_CA_KEY $SSL_LENGTH"
  RUN "eval openssl req -x509 -new -nodes -key $TMPBASEDIR/$SSL_CA_KEY  -passin pass:$SSL_PASS -sha256 -days 3650 -out $TMPBASEDIR/$SSL_CA_CERT -subj $SSL_SUBJECT"
  RUN "openssl genrsa -out $TMPBASEDIR/$SSL_DEVICE_KEY 2048"
  RUN "eval openssl req -new -key $TMPBASEDIR/$SSL_DEVICE_KEY -out $TMPBASEDIR/$SSL_REQUEST_KEY -subj $SSL_SUBJECT"
  RUN "openssl x509 -req -in $TMPBASEDIR/$SSL_REQUEST_KEY -CA $TMPBASEDIR/$SSL_CA_CERT -CAkey $TMPBASEDIR/$SSL_CA_KEY -CAcreateserial -out $TMPBASEDIR/$SSL_DEVICE_CERT -days 3650 -sha256 -passin pass:$SSL_PASS "
  RUN "openssl pkcs12 -export -in $TMPBASEDIR/$SSL_DEVICE_CERT -inkey  $TMPBASEDIR/$SSL_DEVICE_KEY -name awh -out $TMPBASEDIR/SRC-$SSL_KS_P12 -passin pass:$SSL_PASS -passout pass:$SSL_PASS"
  RUN "keytool -importkeystore -destkeystore $BASEDIR/$SSL_KS_P12 -srckeystore $TMPBASEDIR/SRC-$SSL_KS_P12 -srcstoretype PKCS12 -deststoretype pkcs12 -srcstorepass $SSL_PASS -deststorepass $SSL_PASS"
  RUN "keytool -importkeystore -destkeystore $BASEDIR/$SSL_KS_JKS -srckeystore $TMPBASEDIR/SRC-$SSL_KS_P12 -srcstoretype PKCS12 -deststoretype jks  -srcstorepass $SSL_PASS -deststorepass $SSL_PASS"

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
export SSL_PASS=abcabc
export SSL_LENGTH=2048
export SSL_EXPIRE=3650
export SSL_SUBJECT="'/C=BR/ST=Sao_Paulo/L=Sao_Paulo/O=clusterlab.com.br/OU=Clusterlab/CN=shm/emailAddress=devops@clusterlab.com.br'"

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
CERTIFICATE
RUN "mvn compile jib:build -Dmaven.wagon.http.ssl.insecure=true package"
RUN "docker pull $BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG"
RUN "kind load docker-image $BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG"
RUN "docker image ls"
RUN "kubectl get nodes"
RUN "kubectl delete ns awh" ignore
RUN "kubectl create ns awh"
RUN "kubectl -n awh apply -f awh-deploy.yml"
RUN "kubectl -n awh get all"



