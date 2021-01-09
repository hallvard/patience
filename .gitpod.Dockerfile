FROM gitpod/workspace-full-vnc

USER gitpod

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
             && sdk install java 15.0.1.j9-adpt \
             && sdk default java 15.0.1.j9-adpt"
