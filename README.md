# maven-release-plugin-example


### Cleaning a Release

1. Delete the release descriptor (release.properties)

2. Delete any backup POM files

```
mvn release:clean
```



### Preparing the Release

1. Perform some checks � there should be no uncommitted changes and the project should depend on no SNAPSHOT dependencies

2. Change the version of the project in the pom file to a full release number (remove SNAPSHOT suffix) � in our example � 0.1

3. Run the project test suites

4. Commit and push the changes

5. Create the tag out of this non-SNAPSHOT versioned code

6. Increase the version of the project in the pom � in our example � 0.2-SNAPSHOT

7. Commit and push the changes


### Dry Run

Allows you to run all operations in `release:prepare` goal except for actual commits into SCM.

```
mvn release:prepare -DdryRun=true
```


### Performing the Release

1. Checkout release tag from SCM

2. Build and deploy released code

3. Relies on the output of the Prepare step � the `release.properties`.


```
mvn release:prepare
```

### Hosted Maven2 repository in Nexus

Create maven2 hosted repository in Nexus, for us it's `api-release`


### Upload artifact in Nexus from command line:

```
curl -v --user admin:admin123 --upload-file <filename> http://localhost:8081/repository/<repository-name>/<filename>
```


### Configure the credentials for the nexus-releases server in the global `settings.xml` (%USER_HOME%/.m2/settings.xml):


```
<servers>
   <server>
      <id>nexus-releases</id>
      <username>admin</username>
      <password>admin123</password>
   </server>
</servers>
```

### Cleaning, Preparing and Performing the release


```
mvn release:clean release:prepare release:perform -DreleaseVersion=0.1 -DdevelopmentVersion=0.2-SNAPSHOT
```

Reference: http://www.baeldung.com/maven-release-nexus

# Artifactory

## deploy 

```
	<!-- Repository Information -->
	<distributionManagement>
		<snapshotRepository>
			<id>snapshots</id>
			<name>a0qyc6tley14q-artifactory-primary-0-snapshots</name>
			<url>https://caternberg.jfrog.io/artifactory/libs-snapshot</url>
		</snapshotRepository>
		<repository>
			<id>releases</id>
			<!-- http://localhost:8081/repository/<repository-name> -->
			<!--<url>http://localhost:8081/repository/api-release</url>-->
			<url>https://caternberg.jfrog.io/artifactory/libs-releases</url>
		</repository>
	</distributionManagement>
```

add `distributionManagement`  to `pom.xml` and deploy

```
mvn -s settings-jfrog.xml clean deploy
```

# Links
* https://thihenos.medium.com/maven-release-plugin-a-simple-example-of-package-management-9926506acfb9
* https://danielflower.github.io/multi-module-maven-release-plugin/ssh-authentication.html
* https://www.mojohaus.org/development/performing-a-release.html
* http://www.mojohaus.org/build-helper-maven-plugin/
* https://medium.com/@nanditasahu031/jenkins-pipeline-jfrog-artifactory-and-jenkins-integration-4fed3fc8d556
* https://www.jfrog.com/confluence/display/JFROG/Jenkins+Artifactory+Plugin+-+Release+Management
* https://www.jfrog.com/confluence/display/JFROG/Configuring+Jenkins+Artifactory+Plug-in
* https://www.jfrog.com/confluence/display/JFROG/JFrog+Platform+REST+API#JFrogPlatformRESTAPI-WorkingwiththeJFrogPlatformontheCloud
* https://www.jfrog.com/confluence/display/JFROG/Jenkins+Artifactory+Plugin+-+Release+Management#JenkinsArtifactoryPluginReleaseManagement-JenkinsArtifactoryReleaseStagingAPI
* https://www.jfrog.com/confluence/display/JFROG/Using+Jenkins+With+Pipelines
* https://maven.apache.org/scm/maven-scm-plugin/usage.html