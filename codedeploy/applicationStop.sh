#!/bin/bash
# sudo systemctl stop tomcat9
sudo kill -9 `sudo lsof -t -i:8080`
sudo rm -rf webappone/
sudo rm -rf codedeploy/
sudo rm -f appspec.yml