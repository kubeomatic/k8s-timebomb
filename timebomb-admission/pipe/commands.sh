#Inline
function K_EXTEND_TIMER() { if [ $# -eq 2 ]; then kubectl get deployments -A --selector='kubeomatic-io-timebomb=enabled' --no-headers --output=json | jq -r '.items[]|[(.metadata|.namespace,.name),.kind,(.spec.template.metadata.annotations|."kubeomatic-io-timebomb-sku","kubeomatic-io-timebomb-timer",."kubeomatic-io-timebomb-valid",."kubeomatic-io-timebomb-valid-human")]|@csv'| while IFS=\" read V{1..15}; do  export RC=$(echo $V8 | grep ^$2 | wc -l | awk '{print $1}'); if [ $RC -eq 1 ]; then  echo Patching $V6 $V2/$V4 SKU=$V8 LABEL=$V10 EPOCH=$V12 Expire=\"$V14\"; kubectl -n $V2 patch deployment $V4 --type='json' -p='[{"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid", "value":"'$(expr $(date +"%s") + $(expr 60 \* $1 ) )'"}]'; fi; done ; fi;  }
# K_EXTEND_TIMER 10 /avo/pai/filho

#Expanded
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
            echo Patching $V6 $V2/$V4 SKU=$V8 LABEL=$V10 EPOCH=$V12 Expire=\"$V14\"
            kubectl \
              -n $V2 patch deployment $V4 \
              --type='json' \
              -p='[{"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid", "value":"'$(expr $(date +"%s") + $(expr 60 \* $1 ) )'"}]'
          fi
        done
  fi
}