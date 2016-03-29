# maze-escape
Maze Escape Game in Java

## Story
You are falsely imprisoned by the evil king to endlessly wander the prison maze.  
There are myths of a wizard who once walked these lonesome halls.  
Legend says he left behind a potion which can free you of this place.  

![maze](https://github.com/RonanC/maze-escape/blob/master/screen-shots/maze.png "maze")
![win](https://github.com/RonanC/maze-escape/blob/master/screen-shots/win.png "win")

## Insights
### Uninformed Searches
For the Random Walk and Brute Force DFS/BFS searches I get the co-ordinates for each iteration and pass it back to the enemy object to be painted.
For the other algorithms I found it better to actually compute all the moves first, save them to a queue and then let the enemy object pop each co-ordinate position off this queue.

I found that I still needed to have the Thread.sleep in order for the enemies moved to be painted properly.

I could have had the enemies just go straight to you but I wanted to illustrate that they are following the search algorithms pattern.
For the breadth first search the enemy teleports around the map (as the search makes them do this).

#### Heuristic Searches
For the informed searches I only save the output of the algorithm.  
I save this to a FIFO queue.
When you meet a helper(prisoner) he then lights up the path to your goal (up to N steps), which disappears after N time. 

## Animations
I created various different frames for the player and enemy.  
I used the frames in different contexts throughout the game to create animations.
In conjunction with this I also flipped the images horizontally to give the effect of movement or a shift in lighting.

## Screens
I have some options on start (Using JOption).  
I have introduction and winning scenes.  
The game resets on end.  

## Key controls
I have added various keys for different functions:  
Movement: WASD  
Map: M  
Reset: R  
Quit: ESC  

## Packages
I am using various packages to keep thing modular, encapsulated and well structured.

###Package names:
ie.gmit.sw.ai  
ie.gmit.sw.ai.audio  
ie.gmit.sw.ai.characters  
ie.gmit.sw.ai.fight  
ie.gmit.sw.ai.img  
ie.gmit.sw.ai.maze  
ie.gmit.sw.ai.traversers  
ie.gmit.sw.ai.traversers.uninformed  
ie.gmit.sw.ai.traversers.heuristic  


## Class Diagram
Below is a class diagram:

## Java Docs
I have documented every method, class and package.

## Assets
I created all the image assets myself, hoorah for pixel art!  
You may find various pixel art assets I created at this url:  
https://github.com/RonanC/Pixel-Art  

Sound Effects: http://www.wavsource.com/  
Background Music:
http://www.playonloop.com/music-loops-category/videogame/8-bit/

## References
This video tutorial series set the framework up for me to expand upon:  
https://www.youtube.com/watch?v=I1qTZaUcFX0
