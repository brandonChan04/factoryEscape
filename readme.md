# This is an old completed project and has been pushed to github all at once

To do each task, run the commands in a terminal with the current directory set to the root directory of this project

# Build game
`mvn clean package -DskipTests`

# Run game
`java -jar target/main.jar`

# Run tests and prepare coverage report
`mvn jacoco:prepare-agent test install jacoco:report`

# View coverage report
open `target/site/jacoco/index.html`

# Prepare javadocs report
`mvn javadoc:javadoc`

# View javadocs
open `target/site/apidocs/index.html`

# Prebuilt Artifacts
A prebuilt JAR file of the project can be found at 

`prebuilt-artifacts/main.jar`

run it with

`java -jar prebuilt-artifacts/main.jar`

A prebuilt Javadocs artifact can be found at

`prebuilt-artifacts/apidocs/index.html`