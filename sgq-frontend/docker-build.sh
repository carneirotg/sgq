#!/bin/bash

VERSION=`cat package.json | python3 -c "import sys, json; print(json.load(sys.stdin)['version'])"`

echo "Rodando Yarn Build~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
yarn build

echo "Construindo imagem Docker $VERSION~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
cp -R build/ dockerBuild/
cd dockerBuild/

docker build -f Dockerfile . -t sgq-frontend:latest
docker tag sgq-frontend:latest sgq-frontend:${VERSION}

rm -Rf build/

cd ..
