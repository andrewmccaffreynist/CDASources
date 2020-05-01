#!/bin/bash

rm -rf templates
mkdir templates
ls -1 c83_templates > dir_c83.txt
ls -1 pcc_templates > dir_pcc.txt
cp c83_templates/*.ent templates/
cp pcc_templates/*.ent templates/

