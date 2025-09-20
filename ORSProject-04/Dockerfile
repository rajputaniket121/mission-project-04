# Use the official Tomcat 9.0 image
FROM tomcat:9.0  

COPY target/ORSProject-04.war /usr/local/tomcat/webapps/ORSProject-04.war
EXPOSE 8080
CMD ["catalina.sh", "run"]