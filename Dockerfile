# If you have folders or files which you wish to persist in between containers,
# you must set them up as volumes in `docker-compose.yml` and use the command
# `docker-compose up`.
FROM zenika/kotlin:1.3-jdk8

ENV directory /app/
WORKDIR ${directory}

# Copy distribution archive
ADD game/build/distributions/*.zip .

# Unzip distribution archive
RUN unzip -q -o *.zip

# Delete distribution archive after unzipping
RUN rm -fv *.zip

# Move distribution files from "game-${version}" to $WORKDIR ["."]
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