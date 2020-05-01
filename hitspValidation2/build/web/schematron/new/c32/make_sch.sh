#!/bin/bash

echo "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
echo "<!DOCTYPE schema ["

C28ENTITIES=`cat dir_c28.txt`
C32ENTITIES=`cat dir_c32.txt`


for a in $C32ENTITIES
do
	echo "<!ENTITY ent-"${a:0:`expr length $a`-4}" SYSTEM 'templates/"$a"'>"
done

echo
echo

for i in $C28ENTITIES
do
	echo "<!ENTITY ent-"${i:0:`expr length $i`-4}" SYSTEM 'templates/"$i"'>"
done

echo "]>"
echo "   <schema xmlns=\"http://www.ascc.net/xml/schematron\" xmlns:cda=\"urn:hl7-org:v3\">"
echo "   <title>HITSP_C32</title>"
echo "   <ns prefix=\"cda\" uri=\"urn:hl7-org:v3\"/>"
echo "   <ns prefix=\"sdtc\" uri=\"urn:hl7-org:sdtc\"/>"
echo "   <ns prefix=\"xsi\" uri=\"http://www.w3.org/2001/XMLSchema-instance\"/>"
echo
echo "   <phase id='errors'>"


for b in $C32ENTITIES
do
        echo "      <active pattern='p-"${b:0:`expr length $b`-4}"-errors'/>"
done

echo
echo

for j in $C28ENTITIES
do
	echo "      <active pattern='p-"${j:0:`expr length $j`-4}"-errors'/>"
done

echo "   </phase>"
echo
echo "   <phase id='warning'>"

for c in $C32ENTITIES
do
        echo "      <active pattern='p-"${c:0:`expr length $c`-4}"-warning'/>"
done

echo
echo

for k in $C28ENTITIES
do
	echo "      <active pattern='p-"${k:0:`expr length $k`-4}"-warning'/>"
done

echo "   </phase>"
echo
echo "   <phase id='note'>"

for d in $C32ENTITIES
do
        echo "      <active pattern='p-"${d:0:`expr length $d`-4}"-note'/>"
done

echo
echo

for l in $C28ENTITIES
do
	echo "      <active pattern='p-"${l:0:`expr length $l`-4}"-note'/>"
done

echo "   </phase>"
echo
echo

for e in $C32ENTITIES
do
        echo "   &ent-"${e:0:`expr length $e`-4}";"
done

echo
echo

for m in $C28ENTITIES
do
	echo "   &ent-"${m:0:`expr length $m`-4}";"
done

echo "</schema>"
