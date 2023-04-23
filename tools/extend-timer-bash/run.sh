function K_EXTEND_TIMER() {
  if [ $# -eq 2 ]
  then
    kubectl get deployments \
      -A \
      --selector='kubeomatic-io-timebomb=enabled' \
      --no-headers \
      --output=json | \
        jq -r '.items[]|[(.metadata|.namespace,.name),.kind,(.spec.template.metadata.annotations|."kubeomatic-io-timebomb-sku","kubeomatic-io-timebomb-timer",."kubeomatic-io-timebomb-valid",."kubeomatic-io-timebomb-valid-human")]|@csv'| \
        while IFS=\" read V{1..15}
        do
          export RC=$(echo $V8 | grep ^$2 | wc -l | awk '{print $1}')
          if [ $RC -eq 1 ]
          then
            export EPOCH=$(expr $(date +"%s") + $(expr 60 \* $1 ) )
            echo Patching $V6 $V2/$V4 SKU=$V8 LABEL=$V10 EPOCH=$V12 CurrentExpire=\"$V14\" Expire=\"$(date -r $EPOCH  "+%Y/%m/%d_%H:%M:%S")\"
            kubectl \
              -n $V2 patch deployment $V4 \
              --type='json' \
              -p='[
                {"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid", "value":"'$EPOCH'"},
                {"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid-human", "value":"'$(date -r $EPOCH  "+%Y/%m/%d_%H:%M:%S")'"}]'
          fi
        done
  fi
}

function ADD_DATA() {
  export EPOCH=$(expr $(date +"%s") - 10800 )
  kubectl -n $1  get deployments --no-headers| \
    awk ' {print $1}' | \
    while read DP
    do
      kubectl \
              -n $1 patch deployment $DP \
              --type='json' \
              -p='[
              {"op": "add", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid", "value":"'$EPOCH'"},
              {"op": "add", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid-human", "value":"'$(date -r $EPOCH  "+%Y/%m/%d_%H:%M:%S")'"},
              {"op": "add", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-timer", "value":"1m"},
              {"op": "add", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-sku", "value":"/avo/pai/filho"},
              {"op": "add", "path": "/spec/template/metadata/labels/kubeomatic-io-timebomb", "value":"enabled"},
              {"op": "add", "path": "/metadata/labels/kubeomatic-io-timebomb", "value":"enabled"}]'
    done
    kubectl \
      patch ns $1 \
      --type='json' \
      -p='[{"op": "add", "path": "/metadata/labels/kubeomatic-io-timebomb", "value":"enabled"}]'
}

#!/bin/bash
function TIMEBOMB() {
  for BIN in {kubectl,jq,column,date,expr}; do if [ $(which $BIN | wc -l | awk '{print $1} ') -eq 0 ]; then echo $BIN not found ; break; else if [ $(file "$(which $BIN)" 1>&1 >/dev/null;echo $?) -ne 0 ]; then echo $BIN not found; break; fi;fi ;done
  function MKTEMP() {
    case $1 in
      create)
        mktemp
        ;;
      delete)
        rm -f $2
        ;;
    esac
  }
  function GET_K8S_NS_LABEL() {
    kubectl get ns $NS -o json | jq -r '.metadata.labels|."'$1'"'
  }
  function GET_K8S_DEPLOYMENTS() {
    kubectl -n $NS get deployment --no-headers | awk '{print $1}'
  }
  function GET_K8S_DEPLOYMENT_LABEL() {
    while read DPLY
    do
      kubectl -n $NS get deployment $DPLY -o json  | \
        jq -r '''
        [
          { "deployment-label-kubeomatic-io-timebomb": .metadata.labels."kubeomatic-io-timebomb"},
          (
            .spec.template.metadata|
            (
              .annotations|
              {"pod-annoatation-kubeomatic-io-timebomb-valid": ."kubeomatic-io-timebomb-valid"},
              {"pod-annotation-kubeomatic-io-timebomb-valid-human": ."kubeomatic-io-timebomb-valid-human"},
              {"pod-annotation-kubeomatic-io-timebomb-timer": ."kubeomatic-io-timebomb-timer"},
              {"pod-annotation-kubeomatic-io-timebomb-sku": ."kubeomatic-io-timebomb-sku"}
            ),
            {"pod-label-kubeomatic-io-timebomb": .labels."kubeomatic-io-timebomb"}
          )
        ] |
        .[]|
        to_entries|
        map_values(.key + " = " + .value)|
        add|
        @text
      ''' | column -t
    done
  }
  function GET_K8S_NUMBER_OF_PODS_RUNNING() {
    kubectl -n $NS get pods --no-headers | wc -l | awk '{print $1}'
  }
  function GET_DEPLOYMENTS_WITH_TIMEBOMB_ENABLED_AT_NAMESPACE() {
    kubectl -n $NS get deployments  -o json | \
      jq -r '''
        .items[]|
        select(
          .metadata.labels."kubeomatic-io-timebomb" == "enabled"
        )|
        .metadata.name
      '''
  }
  function GET_TIMEBOMB_VALIDITY_BY_NAMESPACE(){
    export NSENABLED="$(GET_K8S_NS_LABEL kubeomatic-io-timebomb)"
    export NSCLUSTER="$(GET_K8S_NS_LABEL kubeomatic-io-timebomb-cluster)"
    echo """TIMEBOMB detail

Namespace DATA
namespace-kubeomatic-io-timebomb=$NSENABLED
namespace-kubeomatic-io-timebomb-cluster=$NSCLUSTER

    """

    echo Deployment DATA
    GET_K8S_DEPLOYMENTS | GET_K8S_DEPLOYMENT_LABEL

    echo ""
    echo Number of PODs runnig: $(GET_K8S_NUMBER_OF_PODS_RUNNING)
  }
  function GET_TIMEBOMB_VALIDITY_BY_SKU() {
    export DATAFILE=$(MKTEMP create)
    if [ "$HEADER" == "true" ]
    then
      echo "\
      pod-annotation-kubeomatic-io-timebomb-sku" \
      pod-annoatation-kubeomatic-io-timebomb-valid \
      pod-annotation-kubeomatic-io-timebomb-valid-human \
      NAMESPACE \
      DEPLOYMENT_NAME \
      deployment-label-kubeomatic-io-timebomb \
      pod-label-kubeomatic-io-timebomb \
      > $DATAFILE
    fi
    kubectl get deployments -A --selector='kubeomatic-io-timebomb=enabled' -o json | \
      jq -r '''
        .items[] |
        select(.metadata.labels."kubeomatic-io-timebomb"=="enabled") |
        select(.spec.template.metadata.labels."kubeomatic-io-timebomb"=="enabled") |
        [

          (
            .spec.template.metadata.annotations|
            {"pod-annotation-kubeomatic-io-timebomb-sku": ."kubeomatic-io-timebomb-sku"},
            {"pod-annoatation-kubeomatic-io-timebomb-valid": ."kubeomatic-io-timebomb-valid"},
            {"pod-annotation-kubeomatic-io-timebomb-valid-human": ."kubeomatic-io-timebomb-valid-human"}
            # {"pod-annotation-kubeomatic-io-timebomb-timer": ."kubeomatic-io-timebomb-timer"}

          ),
          {NameSpace: .metadata.namespace},
          {DeploymentName: .metadata.name},
          {"deployment-label-kubeomatic-io-timebomb": .metadata.labels."kubeomatic-io-timebomb"},
          {"pod-label-kubeomatic-io-timebomb": .spec.template.metadata.labels."kubeomatic-io-timebomb"}
        ]|
        add |
        map_values([.])|add | @tsv
      ''' | grep ^$SKU >> $DATAFILE
      cat $DATAFILE | column -t
      MKTEMP delete $DATAFILE
  }
  function K_EXTEND_TIMER() {
    if [ $# -eq 2 ]
    then
      kubectl get deployments \
        -A \
        --selector='kubeomatic-io-timebomb=enabled' \
        --no-headers \
        --output=json | \
          jq -r '''
          .items[]|
          [
            (
              .metadata|
              .namespace,
              .name
            ),
            .kind,
            (
              .spec.template.metadata.annotations|
              ."kubeomatic-io-timebomb-sku",
              ."kubeomatic-io-timebomb-timer",
              ."kubeomatic-io-timebomb-valid",
              ."kubeomatic-io-timebomb-valid-human"
            )
          ]|
          @csv
          '''| \
          while IFS=\" read V{1..15}
          do
            export RC=$(echo $V8 | grep ^$2 | wc -l | awk '{print $1}')
            if [ $RC -eq 1 ]
            then
              export EPOCH=$(expr $(date +"%s") + $(expr 60 \* $1 ) )
              echo Patching $V6 $V2/$V4 SKU=$V8 LABEL=$V10 EPOCH=$V12 CurrentExpire=\"$V14\" Expire=\"$(date -r $EPOCH  "+%Y/%m/%d_%H:%M:%S")\"
              kubectl \
                -n $V2 patch deployment $V4 \
                --type='json' \
                -p='[
                  {"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid", "value":"'$EPOCH'"},
                  {"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid-human", "value":"'$(date -r $EPOCH  "+%Y/%m/%d_%H:%M:%S")'"}]'
            fi
          done
    fi
  }
  function EXTEND_TIMEBOMB_VALID_FROM_DEPLOYMENMT() {
    export NOWH=$(date)
    export NOWL=$(date +"%s")
    export DATAL=$(expr $NOWL + $(expr $NEWTIMER \* 60 ) )
    # export DATAH=$(date -r $DATAL  "+%Y/%m/%d_%H:%M:%S")
    export DATAH="$(date -d @$DATAL "+%Y/%m/%d_%H:%M:%S")"
    echo Patching $NS/$1 EPOCH=$DATAL WillExpire=\"$DATAH\", NOW is $NOWH -\> $NOWL
    kubectl \
      -n $NS patch deployment $1 \
      --type='json' \
      -p='[
        {"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid", "value":"'$DATAL'"},
        {"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid-human", "value":"'$DATAH'"}]'
  }

  function EXTEND_TIMEBOMB_VALIDITY_BY_NAMESPACE() {
    export NSENABLED="$(GET_K8S_NS_LABEL kubeomatic-io-timebomb)"
    if [ "$NSENABLED" == "enabled" ]
    then
      GET_DEPLOYMENTS_WITH_TIMEBOMB_ENABLED_AT_NAMESPACE |
      while read DPLY
      do
        EXTEND_TIMEBOMB_VALID_FROM_DEPLOYMENMT $DPLY
        DELETE_K8S_REPLICASET_BY_NAMESPACE
      done
    fi
  }

  function GET_DEPLOYMENTS_WITH_TIMEBOMB_ENABLED_BY_SKU() {
    kubectl  get deployments -A -o json | \
      jq -r '''
        .items[]|
        select(.metadata.labels."kubeomatic-io-timebomb" == "enabled")|
        [
          .spec.template.metadata.annotations."kubeomatic-io-timebomb-sku",
          .metadata.namespace,
          .metadata.name
        ]|
        @tsv
      ''' | \
      grep ^$SKU


  }
  function DELETE_K8S_REPLICASET_BY_NAMESPACE() {
    export RPSET=$(kubectl -n $NS get replicaset --no-headers | \
      awk '{print $1}' | tr '\n' ' ')
    kubectl -n $NS delete replicaset $RPSET
  }

  function EXTEND_TIMEBOMB_VALIDITY_BY_SKU() {
    GET_DEPLOYMENTS_WITH_TIMEBOMB_ENABLED_BY_SKU | \
    while read SKU NS DPLY
      do
        export SKU=$SKU
        export DPLY=$DPLY
        export NS=$NS
        EXTEND_TIMEBOMB_VALID_FROM_DEPLOYMENMT $DPLY
        DELETE_K8S_REPLICASET_BY_NAMESPACE
      done
  }
  if [ $# -ne 2 ] && [ $# -ne 3 ]
  then
    echo Not Enought Arguments
    exit 1
  fi
  case $1 in
    GET_TIMEBOMB_VALIDITY_BY_NAMESPACE)
      export NS="$2"
      GET_TIMEBOMB_VALIDITY_BY_NAMESPACE
      ;;
    GET_TIMEBOMB_VALIDITY_BY_SKU)
      export SKU="$2"
      GET_TIMEBOMB_VALIDITY_BY_SKU
      ;;
    EXTEND_TIMEBOMB_VALIDITY_BY_NAMESPACE)
      export NS="$2"
      export NEWTIMER=$3
      EXTEND_TIMEBOMB_VALIDITY_BY_NAMESPACE
      ;;
    EXTEND_TIMEBOMB_VALIDITY_BY_SKU)
      export SKU="$2"
      export NEWTIMER=$3
      EXTEND_TIMEBOMB_VALIDITY_BY_SKU
      ;;
  esac
}
export HEADER="true"

# TIMEBOMB GET_TIMEBOMB_VALIDITY_BY_NAMESPACE nginx

# TIMEBOMB GET_TIMEBOMB_VALIDITY_BY_SKU "/avo/pai/filho"
# TIMEBOMB GET_TIMEBOMB_VALIDITY_BY_SKU "/tribo"
# TIMEBOMB GET_TIMEBOMB_VALIDITY_BY_SKU "/tribo/squad/app"

# TIMEBOMB EXTEND_TIMEBOMB_VALIDITY_BY_NAMESPACE nginx 60
# TIMEBOMB EXTEND_TIMEBOMB_VALIDITY_BY_SKU "/tribo/squad/app" 60
# TIMEBOMB EXTEND_TIMEBOMB_VALIDITY_BY_SKU "/" 60