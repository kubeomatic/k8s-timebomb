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
    coproc stdbuf -o0 sed "s/"$1"/***/g" &
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
    echo $1 | eval "$(CENSOR_BUILD $CENSORSTRING)"
    time eval $1 | eval "$(CENSOR_BUILD $CENSORSTRING)"
    if [ $? -ne 0 ]
    then
      echo FAIL $?
      echo FAIL $1 | eval "$(CENSOR_BUILD $CENSORSTRING)"
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
      echo SUCCESS $1 | eval "$(CENSOR_BUILD $CENSORSTRING)"
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
  if [[ "$SSL_SUBJECT" == "" ]]
  then
    export SSL_SUBJECT="'/C="$SSL_CERT_C"/ST="$SSL_CERT_ST"/L="$SSL_CERT_L"/O="$SSL_CERT_O"/OU="$SSL_CERT_OU"/CN="$K8S_NAME"-admission-service."$K8S_NAMESPACE".svc.cluster.local/emailAddress="$SSL_CERT_EMAIL"'"
  fi
  if [[ "$SSL_ALTNAME" == "" ]]
  then
    export SSL_ALTNAME="subjectAltName=DNS:"$K8S_NAME"-admission-service."$K8S_NAMESPACE".svc.cluster.local,DNS:"$K8S_NAME"-admission-service."$K8S_NAMESPACE".svc"
  fi
  echo $SSL_SUBJECT
  echo $SSL_ALTNAME



  RUN "openssl genrsa -des3 -passout pass:$SSL_PASS -out $TMPBASEDIR/$SSL_CA_KEY $SSL_LENGTH"
  RUN "openssl req -x509 -new -nodes -key $TMPBASEDIR/$SSL_CA_KEY  -passin pass:$SSL_PASS -sha256 -days 3650 -out $TMPBASEDIR/$SSL_CA_CERT -subj $SSL_SUBJECT"
  RUN "openssl genrsa -out $TMPBASEDIR/$SSL_DEVICE_KEY $SSL_LENGTH"
  RUN "openssl req -new -key $TMPBASEDIR/$SSL_DEVICE_KEY -out $TMPBASEDIR/$SSL_REQUEST_KEY -subj $SSL_SUBJECT"
  RUN "openssl x509 -req -in $TMPBASEDIR/$SSL_REQUEST_KEY -CA $TMPBASEDIR/$SSL_CA_CERT -CAkey $TMPBASEDIR/$SSL_CA_KEY -CAcreateserial -out $TMPBASEDIR/$SSL_DEVICE_CERT -days 3650 -sha256 -passin pass:$SSL_PASS -extfile <(printf "$SSL_ALTNAME")"
  RUN "openssl pkcs12 -export -in $TMPBASEDIR/$SSL_DEVICE_CERT -inkey  $TMPBASEDIR/$SSL_DEVICE_KEY -name awh -out $TMPBASEDIR/SRC-$SSL_KS_P12 -passin pass:$SSL_PASS -passout pass:$SSL_PASS"
  RUN "keytool -importkeystore -destkeystore $TMPBASEDIR/$SSL_KS_P12 -srckeystore $TMPBASEDIR/SRC-$SSL_KS_P12 -srcstoretype PKCS12 -deststoretype pkcs12 -srcstorepass $SSL_PASS -deststorepass $SSL_PASS"
  RUN "keytool -importkeystore -destkeystore $TMPBASEDIR/$SSL_KS_JKS -srckeystore $TMPBASEDIR/SRC-$SSL_KS_P12 -srcstoretype PKCS12 -deststoretype jks  -srcstorepass $SSL_PASS -deststorepass $SSL_PASS"

}
function HELM_GET_VALUE() {
  cat $1 | yq "$2"
}
# _____ _____ ____ _____
#|_   _| ____/ ___|_   _|
#  | | |  _| \___ \ | |
#  | | | |___ ___) || |
#  |_| |_____|____/ |_|
#
for BIN in {yq,jq,keytool,openssl}
do
  TEST_BIN $BIN
done
#__     ___    ____  ____
#\ \   / / \  |  _ \/ ___|
# \ \ / / _ \ | |_) \___ \
#  \ V / ___ \|  _ < ___) |
#   \_/_/   \_\_| \_\____/
#

export KUBECONFIG=$HOME/.kube/configs/kind
#export SSL_PASS="$(GENPASS | grep ^GOOD | awk '{print $2}' )"
export SSL_CA_KEY=rootCA.key
export SSL_CA_CERT=rootCA.crt
export SSL_DEVICE_KEY=device.key
export SSL_REQUEST_KEY=device.csr
export SSL_DEVICE_CERT=device.crt
export SSL_KS_P12=ks.p12
export SSL_KS_JKS=ks.jks
export SSL_LENGTH=2048
export SSL_EXPIRE=3650
export SSL_CERT_EMAIL=fake@kubeomatic.org
export SSL_CERT_C=BR
export SSL_CERT_ST=Sao_Paulo
export SSL_CERT_L=Sao_Paulo
export SSL_CERT_O=kubeomatic.io
export SSL_CERT_OU=kubeomatic
# export SSL_SUBJECT="'/C=BR/ST=Sao_Paulo/L=Sao_Paulo/O=clusterlab.com.br/OU=Clusterlab/CN=timebomb-admission-service.timebomb.svc.cluster.local/emailAddress=devops@clusterlab.com.br'"
# export SSL_ALTNAME="subjectAltName=DNS:timebomb-admission-service.timebomb.svc.cluster.local,DNS:timebomb-admission-service.timebomb.svc"
export TMPBASEDIR=tmp
export BASEDIR=extra


# ____  ____  _____ ____   _    ____  _____
#|  _ \|  _ \| ____|  _ \ / \  |  _ \| ____|
#| |_) | |_) |  _| | |_) / _ \ | |_) |  _|
#|  __/|  _ <| |___|  __/ ___ \|  _ <| |___
#|_|   |_| \_\_____|_| /_/   \_\_| \_\_____|
#
if [ $# -eq 1 ] && [ -f $1 ]
then
  export VALUES=$1
else
  echo Not Enought Arguments
  exit 1
fi
export SSL_PASS="$(HELM_GET_VALUE $VALUES '.certificate.keyStorePAss')"
export K8S_NAMESPACE=$(HELM_GET_VALUE $VALUES '.common.nameSpace')
export K8S_NAME=$(HELM_GET_VALUE $VALUES '.common.name')
export WORKDIR=$(pwd)
export CENSORSTRING=$SSL_PASS
cd $WORKDIR





# ____  _   _ _   _ ____
#|  _ \| | | | \ | / ___|
#| |_) | | | |  \| \___ \
#|  _ <| |_| | |\  |___) |
#|_| \_\\___/|_| \_|____/
#
# echo $TMPBASEDIR/$SSL_KS_P12
# echo $VALUES
# file $VALUES
# exit
if ! [ -f $TMPBASEDIR/$SSL_KS_P12 ]
then
  if ! [ -f $TMPBASEDIR ]
  then
    RUN "mkdir -p $TMPBASEDIR" ignore
  fi
  CERTIFICATE
else  
  echo Skippping Certificate creation
fi

#RUN "kubectl create ns $K8S_NAMESPACE"

# if [ -f $TMPBASEDIR/$SSL_KS_P12 ]
# then
#   RUN "kubectl -n $K8S_NAMESPACE create secret generic $K8S_NAME-secret  --from-file $TMPBASEDIR/$SSL_KS_P12"
# else  
#   echo Keystore \"$TMPBASEDIR/$SSL_KS_P12\" not found. Could not crete secret.
# fi

