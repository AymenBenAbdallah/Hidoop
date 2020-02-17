#!/bin/bash

# Arrêter les démons hidoop sur les machines distantes
ssh vador "kill \$(jps | grep HdfsServer | awk '{print \$1}')"

ssh pikachu  "kill \$(jps | grep HdfsServer | awk '{print \$1}')"

ssh goldorak  "kill \$(jps | grep HdfsServer | awk '{print \$1}')"

ssh leia  "kill \$(jps | grep HdfsServer | awk '{print \$1}')"