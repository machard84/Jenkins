FROM registry.chardma.org.uk:8443/centos:7

# packaging dependencies
RUN yum install -y rpm-build
RUN rm -rf /var/cache/yum/*

# packaging
ARG PKG_VERS
ARG PKG_REV
ARG RUNTIME_VERSION
ARG DOCKER_VERSION

ENV VERSION $PKG_VERS
ENV RELEASE $PKG_REV
ENV DOCKER_VERSION $DOCKER_VERSION
ENV RUNTIME_VERSION $RUNTIME_VERSION

# output directory
ENV DIST_DIR=/usr/src/nvidia-container-runtime-$PKG_VERS/SOURCES
RUN mkdir -p ${DIST_DIR}/dist
RUN ls -l ${DIST_DIR}

COPY nvidia-docker ${DIST_DIR}
COPY daemon.json ${DIST_DIR}

WORKDIR ${DIST_DIR}/..
COPY rpm .

RUN sed -i "s;@VERSION@;${PKG_VERS};" ${DIST_DIR}/nvidia-docker

CMD rpmbuild --clean -bb \
             -D "_topdir ${PWD}" \
             -D "version ${VERSION}" \
             -D "release ${RELEASE}" \
             -D "docker_version ${DOCKER_VERSION}" \
             -D "runtime_version ${RUNTIME_VERSION}" \
             SPECS/nvidia-docker2.spec && \
    mv RPMS/noarch/*.rpm /dist