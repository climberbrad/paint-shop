#!/bin/bash

FILE=$1

[ $# -ne 1 ] && { echo "Usage: $0 <data_file>"; exit 1; }

if [[ ! -f $FILE ]];then
   echo "File $FILE does not exist."
   exit 1;
fi

java -cp target/paint-shop-1.0-SNAPSHOT.jar com.bjordan.paintshop.App $FILE
