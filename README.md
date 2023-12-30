# PomModuleWebhook

This is a webhook for the Maven build system. It will send a GET request to a specified URL when the project is built.

## Usage

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.tenitx</groupId>
            <artifactId>PomModuleWebhook</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                <webhookUrl>https://example.com/maven-update/webhook</webhookUrl>
            </configuration>
            <executions>
                <execution>
                    <id>sendUpdateWebhook</id>
                    <phase>deploy</phase>
                    <goals>
                        <goal>updateWebhook</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

This will make a GET request to 
```
https://example.com/maven-update/webhook?artifact={artifact}
```
where `{artifact}` is the artifactId of the project.
