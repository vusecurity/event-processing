FROM eclipse-temurin:17-jre-alpine

# Create cep user and group
RUN addgroup -S cep && adduser -S cep -G cep

# Copy application jar with correct ownership
COPY --chown=cep:cep ./complex-event-processing/target/complex-event-processing-*.jar /home/cep/app.jar

# Setup required directories with correct permissions
USER root
RUN chown -R cep /var/log/ && \
    mkdir /home/cep/license && \
    chown -R cep:cep /home/cep/license

# Switch to non-root user
USER cep

# Set Java options
ENV JAVA_OPTS="-Despertech.license=/home/cep/license/espertech.license"