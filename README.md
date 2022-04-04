# SonOyuncuClient
 playground.
###### Contents
* [Nasıl kullanırım?](#Usage)

# TODO
- Session ID Generator via Native

## Usage
```java
package net.minecraft.client;
...
public class Minecraft implements IThreadListener, IPlayerUsage {
    ...
    private void startGame() throws LWJGLException {
        ...
        this.checkGLError("Post startup");
        //Initialize here.
        SOPacket.initialize();
        ...
    }
}
```