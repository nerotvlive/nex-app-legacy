[![Main branch](https://img.shields.io/badge/NEX_App_(4.0)-%2300000000.svg?style=for-the-badge)](https://github.com/nerotvlive/nex-app)
![Legacy branch (selected)](https://img.shields.io/badge/NEX_App_(Legacy/3.0)-white.svg?style=for-the-badge)
[![Zyneon Application repository](https://img.shields.io/badge/Zyneon_Application_(Old/1.0)-%2300000000.svg?style=for-the-badge)](https://github.com/nerotvlive/zyneon-application)

- - -

# NEX App (Legacy)

by **Zyneon Apex**, a **Zyneon Studios** division

[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://github.com/nerotvlive/nex-app/blob/legacy/LICENSE.md)
- - -

![Logo](https://i.ibb.co/tMsCRxPB/nexapp.png)

The Legacy NEX App is a Minecraft: Java Edition installer, and launcher with some modding support. It integrates CurseForge, Modrinth and our platform "NEX".
<br><br>

[![Download legacy app](https://img.shields.io/badge/Download_legacy_(this)_NEX_App-blue.svg?style=for-the-badge)](https://github.com/nerotvlive/nex-app-legacy/releases/latest)
[![Download new app](https://img.shields.io/badge/Download_new_NEX_App-purple.svg?style=for-the-badge)](https://github.com/nerotvlive/nex-app/releases/latest)


- - -

![App Screenshot](https://raw.githubusercontent.com/nerotvlive/nex-app-legacy/refs/heads/main/src/main/html/assets/nexus/img/app-screenshot.png)

### All in one

Download modpacks from CurseForge, Modrinth and NEX (our platform)


###### This README is work in progress...

- - -

### Dependencies

##### JAR File (only way to run the app for macOS)
- Java 25, we recommend [Azul Zulu JDK](https://www.azul.com/downloads/?version=java-25-lts&package=jdk#zulu)
- The native webview runtimes for your operating system if you are on Windows 10 and Linux. On Windows 11 and macOS they should be pre-installed.

#### Windows 10+:
- [Microsoft Edge WebView2](https://developer.microsoft.com/microsoft-edge/webview2)
  - Should be pre-installed on Windows 11 systems but if it is not, you can download it from the official Microsoft website linked above.
- If you want to launch the .jar you have to install Java 25, we recommend [Azul Zulu JDK](https://www.azul.com/downloads/?version=java-25-lts&os=windows&package=jdk#zulu).

#### Linux:
##### Arch based (Arch, CachyOS & Co)
```bash
sudo pacman -S gtk4 webkit2gtk-4.1 webkitgtk-6.0 jdk25-openjdk --needed
```

##### Debian based (Debian, Ubuntu, ZorinOS & Co):
```bash
sudo apt-get install libgtk-4-1 libwebkit2gtk-4.1-0 libwebkitgtk-6.0-4 libjavascriptcoregtk-6.0-1 openjdk-25-jdk -y
```

##### Other distros:
You need webkit/webkitgtk support and a Java 25 jdk.

- - -

###### The NEX App is an independent project and not affiliated with Minecraft, Mojang AB, Mojang Studios, XBOX, XBOX Game Studios, Microsoft, Rinth Inc., Spark Universe, Modrinth, Overwolf or CurseForge.
