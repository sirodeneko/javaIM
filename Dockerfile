FROM tomcat:9.0.20-jre11
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY IM_war/IM_war.war /usr/local/tomcat/webapps/ROOT.war