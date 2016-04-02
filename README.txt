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
# Maze Escape
Ronan Connolly
G00274374
`2nd April 2016`

## Github Url  
Download the project from here:  
https://github.com/RonanC/maze-escape

## Dropbox URL
Alternatively you can download the zipped jar from dropbox here:  
https://www.dropbox.com/s/tqyv7856g5f5e24/maze-escape.zip?dl=0

## Instructions to run
From within the git home directory (or dropbox zip), in the terminal run `java -jar maze-escape.jar`.   

## Issues
If you have issues running the program from the jar, then try the dropbox url OR try running it from Eclipse.  
I have tested the dropbox zipped project on Windows and OSX, it works fine.  
If there are still issues then run `ant` from the home directory, this will process the build.xml file.

### Terminal Output
This will allow you to view the terminal output which may be useful.  

Algorithms used for the bomb, helpers and various enemies and number of enemies created are shown.  
Fuzzy logic output is also shown.  

## Threads
Threads are used for audio, search algorithms and enemy movement.  

## Note Worthy
I added the most important points into the menu system.  

Try out the maze at dimensions from 20 up to 90.  

Play with each different enemy while the zoom distance is at `farther`;  
This allows you to view the enemies algorithms in real time.  

Make sure to find the potion (goal) at least once, in order to view the win screen.  

Bombs have fire which kill enemies, the fire goes out but leaves scorch marks.  
Bombs go towards the goal up to `N` spaces.  
The helpers (rats) light up a `Yellow Brick Road` towards the goal, up to `N` spaces.

The helpers and bombs use heuristically informed search algorithms.  
The enemies use the uninformed search algorithms.

## Enemies
For the random walk we iterate though each step of the algorithm for each enemy move.  
For the rest of the algorithms I preprocessed the whole algorithm and then when an enemy move needed to be calculated we just popped a position off the completed queue.  
The helpers and bombs also use preprocessing.  
The preprocessing for the iterative deepening algorithm caused my program to crash sometime, so I made everything multi-threaded and it worked smoother.

## Game Flow
I spent a lot of time making sure the game is challenging.  
I did user testing and with the feedback I received I continuously updated and tweaked the game engine.  
Things like:
- The damage in combat, walking, and from map usage
- How long the bombs burn for
- How long the bombs and helpers length should be
- How many of each item there should be
- The look of the menu system at the start for the options

## Assets
I created all the image assets myself.  

## Robustness
The structure is highly modular and encapsulated in order to aid extensibility and understanding.  
JavaDocs and a Class diagram are included.

## Load
The game uses up as much CPU and Memory that is available, but it will run on low powered machine.  
I tested it on various computers to ensure this.

## Commits
I have over 76 commit to this project, all outlining the various stages of the creation.

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