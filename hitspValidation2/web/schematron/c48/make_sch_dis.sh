#!/bin/bash

echo "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
echo "<!DOCTYPE schema ["

PCCENTITIES=`cat dir_pcc.txt`
C83ENTITIES=`cat dir_c83.txt`


for a in $C83ENTITIES
do
	if [ $a != "C48_ref.ent" ];
	then
		echo "<!ENTITY ent-"${a:0:`expr length $a`-4}" SYSTEM 'templates/"$a"'>"
	fi
done

echo
echo

for i in $PCCENTITIES
do
	echo "<!ENTITY ent-"${i:0:`expr length $i`-4}" SYSTEM 'templates/"$i"'>"
done

echo "]>"
echo "   <schema xmlns=\"http://purl.oclc.org/dsdl/schematron\" xmlns:cda=\"urn:hl7-org:v3\" queryBinding=\"xslt2\">"


echo "   <title>HITSP_C48_Discharge</title>"
echo "   <ns prefix=\"cda\" uri=\"urn:hl7-org:v3\"/>"
echo "   <ns prefix=\"sdtc\" uri=\"urn:hl7-org:sdtc\"/>"
echo "   <ns prefix=\"xsi\" uri=\"http://www.w3.org/2001/XMLSchema-instance\"/>"
echo
echo "   <phase id='errors'>"


for b in $C83ENTITIES
do
        if [ $b != "C48_ref.ent" ];
        then
        	echo "      <active pattern='p-"${b:0:`expr length $b`-4}"-errors'/>"
        fi
done

echo
echo

for j in $PCCENTITIES
do
	echo "      <active pattern='p-"${j:0:`expr length $j`-4}"-errors'/>"
done

echo "   </phase>"
echo
echo "   <phase id='warning'>"

for c in $C83ENTITIES
do
        if [ $c != "C48_ref.ent" ];
        then
        	echo "      <active pattern='p-"${c:0:`expr length $c`-4}"-warning'/>"
        fi
done

echo
echo

for k in $PCCENTITIES
do
	echo "      <active pattern='p-"${k:0:`expr length $k`-4}"-warnings'/>"
done

echo "   </phase>"
echo
echo "   <phase id='note'>"

echo "<!-- "

for d in $C83ENTITIES
do
        if [ $d != "C48_ref.ent" ];
        then
        	echo "      <active pattern='p-"${d:0:`expr length $d`-4}"-note'/>"
	fi
done

echo "--> "

echo
echo

echo "<!-- "

for l in $PCCENTITIES
do
	echo "      <active pattern='p-"${l:0:`expr length $l`-4}"-note'/>"
done

echo "--> "

echo "   </phase>"
echo
echo

for e in $C83ENTITIES
do
        if [ $e != "C48_ref.ent" ];
        then
        	echo "   &ent-"${e:0:`expr length $e`-4}";"
	fi
done

echo
echo

for m in $PCCENTITIES
do
	echo "   &ent-"${m:0:`expr length $m`-4}";"
done

echo "</schema>"
