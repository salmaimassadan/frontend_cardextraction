# Utiliser une image OpenJDK
FROM openjdk:11-jdk

# Installer les outils nécessaires
RUN apt-get update && apt-get install -y curl unzip wget && rm -rf /var/lib/apt/lists/*

# Télécharger et installer les outils de ligne de commande Android SDK
RUN wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O /tmp/tools.zip \
    && mkdir -p /sdk/cmdline-tools/latest \
    && unzip /tmp/tools.zip -d /tmp/tools \
    && mv /tmp/tools/cmdline-tools/* /sdk/cmdline-tools/latest \
    && chmod +x /sdk/cmdline-tools/latest/bin/sdkmanager \
    && rm -rf /tmp/tools /tmp/tools.zip

# Accepter les licences et installer les outils nécessaires
RUN yes | /sdk/cmdline-tools/latest/bin/sdkmanager --sdk_root=/sdk --licenses \
    && /sdk/cmdline-tools/latest/bin/sdkmanager "platform-tools" "platforms;android-30"

# Définir les variables d'environnement pour le SDK Android
ENV ANDROID_HOME /sdk
ENV PATH $ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH

# Définir le répertoire de travail
WORKDIR /workspace

# Copier les fichiers du projet dans le conteneur
COPY . .

# Commande par défaut pour lancer le conteneur
CMD ["bash"]