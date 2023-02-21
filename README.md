# maven-release-plugin-example

This is an example on how to use the maven release plugin in conjunction with Artifactory.
To get it working: 
```
mv RENAMEME_settings-jfrog.xml settings-jfrog.xml
#and adjust your jfrog values 
```


## Cleaning a Release

1. Delete the release descriptor (release.properties)

2. Delete any backup POM files

```
mvn release:clean
```

## Preparing the Release

1. Perform some checks there should be no uncommitted changes and the project should depend on no SNAPSHOT dependencies

2. Change the version of the project in the pom file to a full release number (remove SNAPSHOT suffix) in our example 0.1

3. Run the project test suites

4. Commit and push the changes

5. Create the tag out of this non-SNAPSHOT versioned code

6. Increase the version of the project in the pom in our example 0.2-SNAPSHOT

7. Commit and push the changes

## Performing the Release

1. Checkout release tag from SCM

2. Build and deploy released code

3. Relies on the output of the Prepare step the `release.properties`.


## Cleaning, Preparing and Performing the release
```
mvn release:clean release:prepare  release:perform  -DreleaseVersion=0.1 -DdevelopmentVersion=0.2-SNAPSHOT  -s settings-jfrog.xml
## DryRun: mvn -s settings-jfrog.xml release:prepare -DdryRun=true
```

## Submodules
Maven submodules are not in this example

# Deploy to Artifactory 

```
mvn -s settings-jfrog.xml clean deploy
```

# Links
* https://thihenos.medium.com/maven-release-plugin-a-simple-example-of-package-management-9926506acfb9
* https://danielflower.github.io/multi-module-maven-release-plugin/ssh-authentication.html
* https://maven.apache.org/maven-release/maven-release-plugin/
* https://www.mojohaus.org/development/performing-a-release.html
* http://www.mojohaus.org/build-helper-maven-plugin/
* https://medium.com/@nanditasahu031/jenkins-pipeline-jfrog-artifactory-and-jenkins-integration-4fed3fc8d556
* https://www.jfrog.com/confluence/display/JFROG/Jenkins+Artifactory+Plugin+-+Release+Management
* https://www.jfrog.com/confluence/display/JFROG/Configuring+Jenkins+Artifactory+Plug-in
* https://www.jfrog.com/confluence/display/JFROG/JFrog+Platform+REST+API#JFrogPlatformRESTAPI-WorkingwiththeJFrogPlatformontheCloud
* https://www.jfrog.com/confluence/display/JFROG/Jenkins+Artifactory+Plugin+-+Release+Management#JenkinsArtifactoryPluginReleaseManagement-JenkinsArtifactoryReleaseStagingAPI
* https://www.jfrog.com/confluence/display/JFROG/Using+Jenkins+With+Pipelines
* https://maven.apache.org/scm/maven-scm-plugin/usage.html