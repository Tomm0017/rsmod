# If you have folders or files which you wish to persist in between containers,
# you must set them up as volumes in `docker-compose.yml` and use the command
# `docker-compose up` to launch the container.

# Note: You will need to run the `build` gradle task at least once on the server
# before you can run this docker config properly. You will also need to run said
# task whenever you want the docker image to contain the latest changes in your
# code.

# Use kotlin image to run the server.
FROM zenika/kotlin:1.3-eap-jdk8-alpine

# Define `directory` as '/app/'
ENV directory /app/

# Set the work directory as ${directory}
WORKDIR ${directory}

# Copy distribution archive
ADD game/build/distributions/game-shadow-*.zip .

# Unzip distribution archive
RUN unzip -q -o *.zip

# Delete distribution archive after unzipping
RUN rm -fv *.zip

# Move distribution files from "game-${version}" to ${WORKDIR} ["."]
RUN mv game*/* .

# Delete "game-${version}" folder as its contents have been moved
# to working directory
RUN rm -rf game*

# Swap the work directory to the "bin" folder
WORKDIR bin

# Documentation on which ports need to be published when the image is run
# The container must be executed with `-p 43594:43594/tcp`
# For example: docker run -it -p 43594:43594/tcp image
EXPOSE 43594:43594/tcp

# Run the main entry point
ENTRYPOINT ./game