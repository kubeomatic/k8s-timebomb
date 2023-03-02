#!/bin/bash
# _____ _   _ _   _  ____ _____ ___ ___  _   _ ____
#|  ___| | | | \ | |/ ___|_   _|_ _/ _ \| \ | / ___|
#| |_  | | | |  \| | |     | |  | | | | |  \| \___ \
#|  _| | |_| | |\  | |___  | |  | | |_| | |\  |___) |
#|_|    \___/|_| \_|\____| |_| |___\___/|_| \_|____/
#
function GENPASS() {
    #!/bin/bash
    if [ $# -eq 2 ]
    then
        export tamanho=$2
    else
        export tamanho=13
    fi
    function HIGH() {
        # tr -dc A-Za-z0-9!?@#_ < /dev/urandom | head -c ${tamanho} | xargs
        while true
        do
            export PASSWORD=$(tr -dc 'A-Za-z0-9!?@#_' < /dev/urandom | head -c ${tamanho}  | xargs)

            if  [ $(echo $PASSWORD  | sed -e "s/./&\n/g" | egrep "\!|@|#|_|\?" | wc -l ) -gt 2 ] &&  \
                [ $(echo $PASSWORD  | sed -e "s/./&\n/g" | grep "[0-9]" | wc -l ) -gt 2 ] && \
                [ $(echo $PASSWORD  | sed -e "s/./&\n/g" | grep "[a-z]" | wc -l ) -gt 2 ] && \
                [ $(echo $PASSWORD  | sed -e "s/./&\n/g" | grep "[A-Z]" | wc -l ) -gt 2 ]
            then
                echo GOOD $PASSWORD
                break
            else
                echo WEAK $PASSWORD
            fi
        done

    }
    function MEDIUM() {
        tr -dc A-Za-z0-9_ < /dev/urandom | head -c ${tamanho} | xargs
    }
    function LOW() {
        tr -dc A-Za-z0-9 < /dev/urandom | head -c ${tamanho} | xargs
    }
    function NUMBER() {
        tr -dc 0-9 < /dev/urandom | head -c ${tamanho} | xargs
    }
    function HEXA() {
        tr -dc A-F0-9 < /dev/urandom | head -c ${tamanho} | xargs
    }
    function FAKEWWN() {
        tr -dc A-F0-9 < /dev/urandom | head -c 16 | xargs  | sed -e "s/^0x//g" | sed -r "s/../&:/g" | sed -e "s/$://g" | tr "a-z" "A-Z" | sed -e "s/:$//g"
    }
    function HELP() {
        echo HELP
        echo $(echo $0 | tr '\/' '\n' | tail -n 1) \<numero de caracteres\> \| \[ alto \| medio \| baixo \| numero \| hexa \] \<numero de caracteres\>
        echo $(echo $0 | tr '\/' '\n' | tail -n 1) \[ alto \| medio \| baixo \| numero \| hexa \| fakewwn\]
        echo $(echo $0 | tr '\/' '\n' | tail -n 1) \#Opção default utiliza a complexidade alta com $tamanho caracteres
    }
    case $1 in
        alto)
            HIGH
            ;;
        medio)
            MEDIUM
            ;;
        baixo)
            LOW
            ;;
        numero)
            NUMBER
            ;;
        hexa)
            HEXA
            ;;
        fakewwn)
            FAKEWWN
            ;;
        help)
            HELP
            ;;
        ajuda)
            HELP
            ;;
        *)
            re='^[0-9]+$'
            if ! [[ $1 =~ $re ]]
            then
            export tamanho=13
            else
            export tamanho=$1
            fi
            HIGH
            ;;
    esac
}
function CENSOR() {
    coproc stdbuf -o0 sed "s/$1/***/g" &
    while read -r LINE
    do
        echo "$LINE" >&${COPROC[1]}
        read -u ${COPROC[0]} msg
        echo "$msg"
    done
    kill $COPROC_PID
}

function CENSOR_BUILD() {
    export SECRETSTRING=''
    echo $1 | tr ';' '\n' | \
    while read SECRET
    do
        export SECRETSTRING="CENSOR $SECRET | $SECRETSTRING"
        echo $SECRETSTRING
    done | tail -n 1 | sed -e "s/|$//g"
}
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
function TEMPFILE() {
	case $1 in
	create)
		mktemp
		;;
	delete)
		rm  -f $2
		;;
	*)
		EXITNOW "could not create temporary file"
		;;
	esac
}
function RUN() {
  export RC=3
  while true
  do
    date
    echo $1 | eval "$(CENSOR_BUILD $SSL_PASS $CR_USER $CR_PASS)"
    time eval $1 | eval "$(CENSOR_BUILD $SSL_PASS $CR_USER $CR_PASS)"
    if [ $? -ne 0 ]
    then
      echo FAIL $?
      echo FAIL $1 | eval "$(CENSOR_BUILD $SSL_PASS $CR_USER $CR_PASS)"
      case $2 in
        ignore)
          export RC=0
          ;;
        retry)
          if [ $RC -eq 3 ]
          then
            export RC=2
            sleep 10
          else
            export RC=1
            exit 1
          fi
          ;;
        *)
          exit 1
          ;;
      esac
    else
      echo SUCCESS $1 | eval "$(CENSOR_BUILD $SSL_PASS $CR_USER $CR_PASS)"
      export RC=0
    fi
    if [ $RC -eq 0 ]
    then
      break
    fi
  done
}
function CONCAT_CA() {
  export TMPCERT=$(TEMPFILE create)
  cat $1 > $TMPCERT
  cat $2 >> $TMPCERT
  cp $TMPCERT $2
  TEMPFILE delete $TMPCERT
}
function CERTIFICATE() {
  RUN "openssl genrsa -des3 -passout pass:$SSL_PASS -out $TMPBASEDIR/$SSL_CA_KEY $SSL_LENGTH"
  RUN "openssl req -x509 -new -nodes -key $TMPBASEDIR/$SSL_CA_KEY  -passin pass:$SSL_PASS -sha256 -days 3650 -out $TMPBASEDIR/$SSL_CA_CERT -subj $SSL_SUBJECT"
  RUN "openssl genrsa -out $TMPBASEDIR/$SSL_DEVICE_KEY $SSL_LENGTH"
  RUN "openssl req -new -key $TMPBASEDIR/$SSL_DEVICE_KEY -out $TMPBASEDIR/$SSL_REQUEST_KEY -subj $SSL_SUBJECT"
  RUN "openssl x509 -req -in $TMPBASEDIR/$SSL_REQUEST_KEY -CA $TMPBASEDIR/$SSL_CA_CERT -CAkey $TMPBASEDIR/$SSL_CA_KEY -CAcreateserial -out $TMPBASEDIR/$SSL_DEVICE_CERT -days 3650 -sha256 -passin pass:$SSL_PASS -extfile <(printf "$SSL_ALTNAME")"
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
for BIN in {killall,docker,kubectl,kind,mvn,keytool,openssl,pkill,stern}
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
export TAG_COUNT=admission-200
expr $TAG_COUNT + 1 > pipe/build.tag.count
export BUILD_TAG=$TAG_COUNT
export BUILD_TAG_ADMISSION=admission-200
export BUILD_TAG_SCHEDULE=schedule-200
export BUILD_REGISTRY=registry.hub.docker.com:443
export BUILD_DST_IMAGE=kubeomatic/timebomb
export BUILD_DST_IMAGE_ADMISSION=timebomb-admission
export BUILD_DST_IMAGE_SCHEDULE=timebomb-schedule
export BUILD_SRC_IMAGE=mcr.microsoft.com/openjdk/jdk:17-ubuntu
export CR_USER=$(cat etc/cr_user)
export CR_PASS=$(cat etc/cr_pass)
export KUBECONFIG=$HOME/.kube/configs/kind
#export SSL_PASS="$(GENPASS | grep ^GOOD | awk '{print $2}' )"
export SSL_CA_KEY=rootCA.key
export SSL_CA_CERT=rootCA.crt
export SSL_DEVICE_KEY=device.key
export SSL_REQUEST_KEY=device.csr
export SSL_DEVICE_CERT=device.crt
export SSL_KS_P12=ks.p12
export SSL_KS_JKS=ks.jks
export SSL_PASS=abcabc
export SSL_LENGTH=2048
export SSL_EXPIRE=3650
export SSL_SUBJECT="'/C=BR/ST=Sao_Paulo/L=Sao_Paulo/O=clusterlab.com.br/OU=Clusterlab/CN=timebomb-admission-service.timebomb.svc.cluster.local/emailAddress=devops@clusterlab.com.br'"
export SSL_ALTNAME="subjectAltName=DNS:timebomb-admission-service.timebomb.svc.cluster.local,DNS:timebomb-admission-service.timebomb.svc"
export TMPBASEDIR=tmp
export BASEDIR=extra

# ____  ____  _____ ____   _    ____  _____
#|  _ \|  _ \| ____|  _ \ / \  |  _ \| ____|
#| |_) | |_) |  _| | |_) / _ \ | |_) |  _|
#|  __/|  _ <| |___|  __/ ___ \|  _ <| |___
#|_|   |_| \_\_____|_| /_/   \_\_| \_\_____|
#
cat pipe/template-awh-deploy.yml | \
  sed \
    -e 's|TEMPLATE_DEPLOY_IMAGE_ADMISSION|'$BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG_ADMISSION'|g' \
    -e 's|TEMPLATE_DEPLOY_IMAGE_SCHEDULE|'$BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG_SCHEDULE'|g' \
    -e 's|TEMPLATE_VALIDATINGWH_CAROOT|'$(cat $TMPBASEDIR/$SSL_CA_CERT | base64 -w 0)'|g' > awh-deploy.yml
cd $WORKDIR

if ! [ -f $BASEDIR/$SSL_KS_P12 ]
then
  RUN "rm -f $TMPBASEDIR/*" ignore
  RUN "rm -f $BASEDIR/*" ignore
fi
RUN "mkdir -p $BASEDIR" ignore
RUN "mkdir -p $TMPBASEDIR" ignore
#RUN "kind delete cluster ; kind create cluster ; kind get kubeconfig > $HOME/.kube/configs/kind"
RUN "killall kubectl" ignore
# ____  _   _ _   _ ____
#|  _ \| | | | \ | / ___|
#| |_) | | | |  \| \___ \
#|  _ <| |_| | |\  |___) |
#|_| \_\\___/|_| \_|____/
#
if ! [ -f $BASEDIR/$SSL_KS_P12 ]
then
  CERTIFICATE
fi
RUN "kubectl delete -f awh-deploy.yml"
mvn test; if [ $? -ne 0 ]; then echo TEST FAIL; exit 1;fi
RUN "mvn compile jib:build -Dmaven.wagon.http.ssl.insecure=true -Dmaven.test.skip=true package"
RUN "docker pull $BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG"
RUN "docker image ls | grep -w $BUILD_DST_IMAGE:$BUILD_TAG"
RUN "kind load docker-image $BUILD_REGISTRY/$BUILD_DST_IMAGE:$BUILD_TAG"
RUN "docker image ls | grep -w $BUILD_DST_IMAGE:$BUILD_TAG"
RUN "kubectl get nodes"
RUN "kubectl -n timebomb get all"
RUN "kubectl apply -f awh-deploy.yml"
RUN "kubectl -n timebomb get all"
RUN "sleep 5; kubectl -n timebomb port-forward service/awh-service 8443:443" retry  &
export PIDPF=$!
#stern -n awh awh
read
RUN "killall kubectl"
RUN "pkill -P $PIDPF"



