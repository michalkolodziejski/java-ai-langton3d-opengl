java-ai-langton3d-opengl
========================

##Description
Langton's Ant Cellular Automata, 3d implementation in Java and OpenGL (lwjgl stack).

If You need more information about Lanton's Ant - check here: [Wikipedia](http://en.wikipedia.org/wiki/Langton%27s_ant)

![image](https://raw.githubusercontent.com/michalkolodziejski/java-ai-langton3d-opengl/master/images/screenshot_20140513_001.jpg)

##Compile
You need JDK 1.6 and Maven to compile the project.
```shell
    mvn clean
    mvn package
```

## Running
After successful `packaging` there is one interesting artifact, a file named `langton3d-1.0-release.zip`.

1. unpack file `langton3d-1.0-release.zip`
2. `on windows` execute script named `run.bat`
3. `on u*ix` in terminal - execute `chmod +x run.sh` which tells your system to mark file as executable, then execute `./run.sh`. ENJOY.

## Settings (at compile time)
### Ant 'genetic code'
Ant `program` is contained in the following array: `org.mkdev.ai.langton3d.core.App#CODE`.

Program contains elements, called steps, which are as follows:

Step | Description
-- | --
RIGHT | tells ant to move right 
LEFT | tells ant to move left
UP | tells ant to move up
DOWN | tells ant to move down

### World size
You can change dimension (measured in 'cubes' per coordination axis), changing values for these fields:

`org.mkdev.ai.langton3d.core.MapCube#W` changes `width` of laboratory space
`org.mkdev.ai.langton3d.core.MapCube#H` changes `height` of laboratory space
`org.mkdev.ai.langton3d.core.MapCube#D` changes `depth` of laboratory space

### Speed of generation
org.mkdev.ai.langton3d.core.Settings#ITERATIONS

## Credits
***Special thanks to Andrzej Masłowski for asking the right question at the right time.***

It all started while studying as an mid-term project about 2004. Today's version is refactored solution created solely for my own consumption and amusement. Then I've decided to share it with JAVA community.

## History

2014-05-11

* Initial version.

## Problems?

[Submit an issue](https://github.com/michalkolodziejski/java-ai-langton3d-opengl/issues).