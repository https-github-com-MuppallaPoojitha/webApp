#!/bin/bash

# sudo systemctl stop tomcat9

# sudo rm -rf /var/lib/tomcat9/webapp/ROOT

# sudo chown tomcat:tomcat /var/lib/tomcat9/webapp/ROOT.war

# # cleanup log files
# sudo rm -rf /var/lib/tomcat9/logs/catalina*
# sudo rm -rf /var/lib/tomcat9/logs/*.log
# sudo rm -rf /var/lib/tomcat9/logs/*.txt

sudo kill -9 `sudo lsof -t -i:8080`
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ubuntu/cloudwatch_config.json -s
sudo systemctl start amazon-cloudwatch-agent