<h1 align="center">gama</h1>

<h4 align="center">General code for java.</h4>

<p align="center">
    <a href="https://saic.one/discord">
        <img src="https://img.shields.io/discord/974288218839191612.svg?style=flat-square&label=discord&logo=discord&logoColor=white&color=7289da"/>
    </a>
    <a href="https://www.codefactor.io/repository/github/saicone/gama">
        <img src="https://www.codefactor.io/repository/github/saicone/gama/badge?style=flat-square"/>
    </a>
    <a href="https://github.com/saicone/gama">
        <img src="https://img.shields.io/github/languages/code-size/saicone/gama?logo=github&logoColor=white&style=flat-square"/>
    </a>
    <a href="https://mvnrepository.com/artifact/com.saicone/gama">
        <img src="https://img.shields.io/maven-central/v/com.saicone/gama"/>
    </a>
    <a href="https://javadoc.saicone.com/gama/">
        <img src="https://img.shields.io/badge/JavaDoc-Online-green?style=flat-square"/>
    </a>
</p>

A collection of java code for general purposes.

No usage documentation, only javadocs are provided.

## Dependency

build.gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.saicone:gama:1.0.0'
}
```

build.gradle.kts

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.saicone:gama:1.0.0")
}
```

pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>com.saicone</groupId>
        <artifactId>gama</artifactId>
        <version>1.0.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```