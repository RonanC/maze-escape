```
  __  __                          ______                                     
 |  \/  |                        |  ____|                                    
 | \  / |   __ _   ____   ___    | |__     ___    ___    __ _   _ __     ___
 | |\/| |  / _` | |_  /  / _ \   |  __|   / __|  / __|  / _` | | '_ \   / _ \
 | |  | | | (_| |  / /  |  __/   | |____  \__ \ | (__  | (_| | | |_) | |  __/
 |_|  |_|  \__,_| /___|  \___|   |______| |___/  \___|  \__,_| | .__/   \___|
                                                               | |           
                                                               |_|           
```
# maze-escape
Maze Escape Game in Java

## Story
You are falsely imprisoned by the evil king to endlessly wander the prison maze.  
There are myths of a wizard who once walked these lonesome halls.  
Legend says he left behind a potion which can free you of this place.  

[Official Project Brief](aiAssignment2016.pdf).

[Runnable (Jar) Game](https://www.dropbox.com/s/tqyv7856g5f5e24/maze-escape.zip?dl=0)

Youtube Video: https://www.youtube.com/watch?v=3r1qVz7-Eb8  
<a href="http://www.youtube.com/watch?feature=player_embedded&v=3r1qVz7-Eb8
" target="_blank"><img src="http://img.youtube.com/vi/3r1qVz7-Eb8/0.jpg" 
alt="maze-escape" width="480" height="360" border="10" /></a>

### initial game images
![maze](https://github.com/RonanC/maze-escape/blob/master/screen-shots/maze.png "maze")
![win](https://github.com/RonanC/maze-escape/blob/master/screen-shots/win.png "win")

### final game images
![maze](https://github.com/RonanC/maze-escape/blob/master/screen-shots/view.png "view")
![win](https://github.com/RonanC/maze-escape/blob/master/screen-shots/map.png "map")

## General Info
Every time you make N amount of step or look at your map you lose health.  
### Items
- Sword (breaks after two battles)
- Bomb (kills everything within N spaces)
- Med Kit (increases health by N amount)

### Scalability
Everything in the game scales.  
When you change the size of the map, you get a different amount of enemies and items.  
Each enemy has a different intellect level, which is spread out evenly.  
The depth limit of the Depth limited DFS is also scaled to the size of the map.  
As is everything where possible.  

### Efficiency
There is a lot of painting and logic occurring a few times a second.   
This causes the game to use as much CPU as possible.  
I'm also using many threads, for audio and enemy timer tasks.

### Animations
I created various different frames for the player and enemy.  
I used the frames in different contexts throughout the game to create animations.  
In conjunction with this I also flipped the images horizontally to give the effect of movement or a shift in lighting.

### Screens
I have some options on start (Using JOption).  
I have introduction and winning scenes.  
The game resets on end.  

### Key controls
I have added various keys for different functions:  
- Movement: WASD  
- Map: M  
- Bomb: Space  
- Mute BG Tunes: T  
- Quit: ESC  

### Threading
Each enemy is allocated an EnemyTask which is a subclass of TimerTask (which is threaded).  
This allows the enemies movements to be on a separate thread.  
The audio sounds are also threaded.  
Every search algorithm runs on its own thread.  

I found that the iteritive deepening algorithm sometimes crashed my program when it was run on the main thread.  
When run on an independent thread it works perfectly every time.  
So I then made all search algorithms threaded.

## Search Algorithms
The enemies use the six different uninformed search algorithms (there are always at least 6 enemies).  
The helpers (rats) use the informed heuristic searches to show you (half the map length) the way there.  

Bombs do the same, a random heuristic algorithm is chosen and shows you half the map length towards the goal,   
the difference is you can place it where you want and it will cause a firely blast that will kill enemies for N seconds.  

We have all the positions for the search algorithms to the goal but we only show have the maze length amount of spaces.

I have used every algorithm.  
I have implemented them in various ways.

### Uninformed Searches
`ie.gmit.sw.ai.characters.EnemyBrain.java` is where these algorithms are implemented.  

For the Random Walk and Brute Force DFS/BFS searches I get the co-ordinates for each iteration and pass it back to the enemy object to be painted.  
For the other algorithms I found it better to actually compute all the moves first, save them to a queue and then let the enemy object pop each co-ordinate position off this queue.

I found that I still needed to have the Thread.sleep in order for the enemies moved to be painted properly.  

I could have had the enemies just go straight to you but I wanted to illustrate that they are following the search algorithms pattern.  
For the breadth first search the enemy teleports around the map (as the search makes them do this).

#### Heuristic Searches
`ie.gmit.sw.ai.maze.InformedPathMarker.java` is where these algorithms are implemented.  
For the informed searches I only save the output of the algorithm.  
I save this to a FIFO queue.  
When you meet a helper(prisoner) he then lights up the path to your goal (up to N steps), which disappears after N time.

### Custom player placement
This algorithm works by setting the current node at the bottom left and the goal node at the top right.  
We then calculate the heuristic between each node and the actual in game goal.  
We keep track of the worst node, and place the player there.  
We could check the other diagonal as well which might move us even a few more tiles further away but then the player would always be able to guess where abouts the goal is, this way is it not exact, which is better for game-play.

## Fighting
### Fuzzy Logic
Fuzzy logic is used for the fighting.  
I use the JFuzzyLogic library, found [here](http://jfuzzylogic.sourceforge.net/html/index.html).  
The player and enemy statistics are massaged into an even format, which is then passed into the fuzzy class which fuzzifies the values and gives you a solid value back (your score, which equates to damage).  
The damage is then cast onto one another and if either parties health is below 0 then death incurs.  

### Player Statistics
- Health
- Sword
- Luck (Randomness and Steps)

### Enemy Statistics
- Health
- Intelligence
- Luck (Randomness)

## Enemies
### Enemy Generation
There are six enemies generated.  
One for each algorithm.

### Intellect
The enemies are assigned an intellect level each.  
This decides which search algorithm they are given and also affects their outcome in battles.


## Code
### Packages
I am using various packages to keep thing modular, encapsulated and well structured.

####Package names:
- ie.gmit.sw.ai  
- ie.gmit.sw.ai.audio  
- ie.gmit.sw.ai.characters  
- ie.gmit.sw.ai.fight  
- ie.gmit.sw.ai.img  
- ie.gmit.sw.ai.maze  
- ie.gmit.sw.ai.traversers  
- ie.gmit.sw.ai.traversers.uninformed  
- ie.gmit.sw.ai.traversers.heuristic  

### Audio
Each audio object is on its own thread.

### Class Diagram
Below is a class diagram:  
![win](https://github.com/RonanC/maze-escape/blob/master/class_diagrams/Class\ Diagram.png "class diagrams")

### Java Docs
I have documented every method, class and package.

## Misc
### Assets
I created all the image assets myself, hoorah for pixel art!  
You may find various pixel art assets I created at this url:  
https://github.com/RonanC/Pixel-Art  

Sound Effects: http://www.wavsource.com/  
Background Music:  
http://www.playonloop.com/music-loops-category/videogame/8-bit/  

### References
This video tutorial series set the framework up for me to expand upon:   
https://www.youtube.com/watch?v=64V8CC7nSok


```
                                    @@                             
                                    @@                             
                                    @@@@      @@                   
                                    @@@@      @@                   
                         @@@@@@@  @@''@@@@  @@@@@@                 
                         @@@@@@@  @@''@@@@  @@@@@@                 
                         @@''@@@@@@@####''@@@@''@@                 
                         @@''@@@@@@@####''@@@@''@@                 
                         @@''''''';;######++;;##@@                 
                         @@''''''';;######++;;##@@                 
                     @@@@@@##;;;'';;;;######;;##;;@@               
                     @@@@@@##;;;'';;;;######;;##;;@@               
                     @@##@@#####'';;''''####''''''''@@@@@          
                     @@##@@#####'';;''''####''''''''@@@@@          
                     @@###########''''''''''''''##''@@             
                     @@###########''''''''''''''##''@@             
                   @@@@@@@@@@#####........````####@@@@             
                   @@@@@@@@@@#####........````####@@@@             
                   @@##@@..@@@@@::,,..........::@@@@               
                   @@##@@..@@@@@::,,..........::@@@@               
                   @@##@@..@@@@@::,,..........::@@@@               
                   @@##@@..::@@@::####........##@@                 
                   @@##@@..::@@@::####........##@@                 
                     @@@@....,,,,,  ''##::::##''@@                 
                     @@@@....,,,,,  ''##::::##''@@                 
                       @@@@@@@@@,,,,..::....,,::@@                 
                       @@@@@@@@@,,,,..::....,,::@@                 
                           @@+++@@,,....,,::::@@                   
                           @@+++@@,,....,,::::@@                   
                       @@@@;;;;;++@@@@@@@@@@@@                     
                       @@@@;;;;;++@@@@@@@@@@@@                     
                   @@@@'';;;;;;;;;````````'';;@@                   
                   @@@@'';;;;;;;;;````````'';;@@                   
                 @@::::;;@@@@''';;;;``;;;;''++;;@@                 
                 @@::::;;@@@@''';;;;``;;;;''++;;@@                 
               @@::,,@@@@  @@''''';;;;++++@@@@::..@@               
               @@::,,@@@@  @@''''';;;;++++@@@@::..@@               
               @@::@@      @@''';;;;;;;;@@    @@::,,@@             
               @@::@@      @@''';;;;;;;;@@    @@::,,@@             
               @@....@@    @@''';;;;;;;;@@      @@....@@@          
               @@....@@    @@''';;;;;;;;@@      @@....@@@          
               @@,,..@@  @@+++++++''''++@@      @@::..@@@          
               @@,,..@@  @@+++++++''''++@@      @@::..@@@          
                 @@@@    @@;;;;;;;;;;;;;;;@@      @@@@             
                 @@@@    @@;;;;;;;;;;;;;;;@@      @@@@             
                 @@@@    @@;;;;;;;;;;;;;;;@@      @@@@             
                       @@;;;;+++@@@@;;;;++;;@@@@                   
                       @@;;;;+++@@@@;;;;++;;@@@@                   
                       @@''++@@@    @@@@++''''@@                   
                       @@''++@@@    @@@@++''''@@                   
                     @@''++@@           @@''''@@                   
                     @@''++@@           @@''''@@                   
                   @@::..::@@             @@,,..@@@@               
                   @@::..::@@             @@,,..@@@@               
                   @@,,....@@             @@,,..::@@               
                   @@,,....@@             @@,,..::@@               
```
