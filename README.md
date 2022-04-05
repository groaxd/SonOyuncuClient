# SonOyuncuClient
 playground.
###### Contents
* [Nasıl kullanırım?](#Usage)

# TODO
- Maybe how to get Session ID ? (I don't release my auto generator due to pasters)

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
