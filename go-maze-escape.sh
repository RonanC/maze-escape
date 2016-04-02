#! /usr/bin/env sh

ant -v
echo '\n'

rm maze-escape.zip
zip -r maze-escape.zip maze-escape.jar resources/* fcl/*
cp maze-escape.zip ~/Dropbox/jars/
rm maze-escape.zip
