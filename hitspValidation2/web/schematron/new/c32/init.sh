#!/bin/bash

rm -rf templates
mkdir templates
cp mapping.ent c32_templates
ls -1 c32_templates > dir_c32.txt
ls -1 c28_templates > dir_c28.txt
cp c28_templates/*.ent templates/
cp c32_templates/*.ent templates/

