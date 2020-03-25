# CPE203-project
This repo contains the final result of my OOP class program. It simulates interactions between multiple sea creatures, each having different functions.

1. When the event is triggered by left click, it will spawn a trash can that surrounded by polluted water. This is bad for the environment, so the nearest Octo gets infected (see no. 5 to see its new behavior)
2. Right after the trash can is placed, a sea turtle (our new entity) will come out near Atlantis. The sea turtle will swim to the trash and eat it. Then, it becomes infected. 
3. Our infected sea turtle will wait until a fish pops up, and then swim and infect the nearest fish. 
4. The sea turtle can not live much longer so they die after infecting fish (we remove entity from the world). The fish becomes becomes infected.
5. If the octopus eats the infected fish, it gets infected. Infected octo will determine if it’s closer for them to swim to the Atlantis and die peacefully or find the nearest octo and infect them.  
