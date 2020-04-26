# Nimbus
A 2D game engine and software renderer built on a data-driven ECS architecture.

## How does it work?
Nimbus is a zero dependency, simple to use and understand 2D game engine built with Java. It has its own built in software renderer and is thanks to its ECS architecture both highly flexible, scalable as well as straight forward to use. Nimbus has a few classes that you can extend to create the foundation of your game


### The AGame class
AGame is an abstract class that you can extend in your project. This class is the base of your entire game world. Your game object contains a stack of scene objects that make up the state of what is actually happening in the game. A scene stack could, for example, look something like this: 

“Main Menu” -> “Playing Level” -> “Pause Menu”. 

By pushing a new scene onto the stack you can control what state your game is in. You can then sequentially pop the scenes to go backwards.

#### Life cycle methods
A few life cycle methods to be aware of:
Method | Description
--- | ---
Init   |   The Nimbus engine has been initialized. The engine will initialize in a separate thread, and this is your chance to directly communicate with the engine and the renderer before anything else happens.
Dispose   |   The game is shutting down, take care of any necessary cleanup

### The AScene class
AScene is an abstract class that you can extend in your project. A scene is essentially a major state in your game. Each scene has access to an ECS datastore (simply called “datastore”) that is unique to that scene. You can use this datastore to initialize and access your game entities, components and systems, as well as attach event listeners.

#### Life Cycle Methods
A few life cycle methods to be aware of:
Method | Description
--- | ---
Entered   |   The Scene has been pushed onto the stack
Obscuring   |   Another Scene is now on top of this scene in the stack, and this scene will pause
Revealed   |   An obscuring scene has been popped off the stack and this is now the live scene
Exiting   |   This scene is being disposed off, handle any necessary cleanup
Update   |   A data update tick has been requested
Render   |   A render update tick has been requested
PostProcessingFinished   |   The renderer has finished all post processing. This is a great time to add menus and interfaces that go on the very top.

## ECS
Your game consists of entities, components and systems that are all housed inside of an ECS datastore, uniquely accessible on a scene by scene basis.
- **E**ntity    -> Entities are component lists with a unique ID
- **C**omponent -> Components are (mostly) logic-less state containers
- **S**ystem    -> Systems are (mostly) stateless logic executors

The idea of the ECS architecture is to work in a data driven manner - similar to how one would query relational databases. Systems are tasked with carrying out a specific set of operations on valid entities, and are executed in a given order. In a typical object oriented approach you might find yourself traversing a list of entities and then for each entity a list of components. Finally, carrying out some form of component specific logic. In an ECS approach the systems will instead query the datastore for all entities of a certain component composition, and execute all operations relevant to that composition for each entity. I.e. instead of executing all logic types for one entity at a time the system executes one logic type for all relevant entities at a time. This approach makes it a lot easier to troubleshoot, and opens up for writing efficient code.

### Entity
The Entity class is essentially just a UUID and a list of components. An entity can only have *one* instance of the same component. Adding multiple components of the same type will overwrite previous ones. To create an entity you simply create an instance of the Nimbus Entity class. This can then be added to the datastore.

### Component
Components can be created by implementing the Nimbus component interface. There are two types of components interfaces:
Interface | Description
--- | ---
IComponent   |   Attaches to entities
IGlobalComponent   |   Globally accessible state that attaches to the datastore itself (sometimes referred to as “singleton components”)

Your components are used to store state, and should contain virtually no logic at all. IComponent classes are prefixed with a *“C”*. IGlocalComponent classes are prefixed with a *“GC”*.

#### Built in components
There are a few components that come with Nimbus and are ready for use. These include:
Component | Description
--- | ---
CTransform   |   Position and scale (2D vectors)
CBody   |   Measurements (width & height)
CMass   |   Weight
CVelocity   |   Movement
CRectangleShape   |   Rectangle shape representation
CSprite   |   Image representation
CAnimation   |   Animation representation
CAABB   |   AABB collision state
CCamera   |   Camera view state
GCInput   |   Input state
CParticle   |   Particle state
CPlayer   |   Tag to easily find player entity
GCAABBCollissionTree   |   Collision tree

### System
Systems are created by implementing the abstract class ASystem. A system usually interacts either with global components inside of the datastore, or queries the datastore for relevant entities using the query API. All system classes are prefixed with *S*.

Nimbus systems allow you to set a priority. The priority of a system will determine the order in which it is executed on each tick. A lower number means an earlier execution. This way you can sort the order in which you want your systems to execute. For example, you may want your movement system to execute before your collission detection system. You could then set movement to priority 1 and collission detection to priority 2. Additionally systems have a parallel boolean flag that determines if the system can be executed in a parallel, separate thread. It may or may not end up being executed in parallel with other systems, at the discretion of the engine.

#### Life Cycle Methods
A few life cycle methods to be aware of:
Method | Description
--- | ---
Init   |   The system has been assigned to a datastore. (This is also when you get access to the assigned datastore)
Update   |   A data update tick has been requested
Render   |   A render update tick has been requested

#### Built in systems
There are a few systems that come with Nimbus and are ready for use. These include:
Component | Description
--- | ---
SAABBCollision   |   Determine AABB collisions
SMovement   |   Apply velocity to transform
SGravity   |   Apply downwards velocity based on mass
SSpriteAnimation   |   Update and render animations
SSprite   |   Render sprites
SParticle   |   Basic particle system for rendering cool effects
SCamera   |   Camera system that supports different camera styles
SHitboxDebugger   |   Renders all entity hitboxes (bodies)

For more information about how to use the built in systems you can check out the examples included in this repository!

### The datastore
The datastore is the heart of the entire ECS architecture. It creates and manages three different (pretty self explanatory) data managers:

- EntityManager
- ComponentManager
- SystemManager

#### Query API
The datastore exposes a query API that can be used to look up entities that match certain component composition criteria. Queries are created using the Query and Operation classes. A query holds a number of operations that will be executed sequentially to narrow down entities. It is therefore usually a good idea to start with an operation that is as distinct as possible. 

Possible operations types are:
Operation | Description
--- | ---
HasComponent   |   (entity must have component)
NotHasComponent   |   (entity must *not* have component)
InnerJoin   |   (*AND*; remove non-common entities between two queries)
OuterJoin   |   (*OR*; keep entities that are either in query A or query B)

#### Events
Each manager has its own event emitter that is implemented in the datastore in order to be able to create efficient data flows between managers. These event emitters fire events when, for example, a new entity/component/system has been created/deleted. You can jack in directly into these event systems by registering a listener in the datastore. All you need to do is implement the corresponding interface in your class:

Interface | Description
--- | ---
IEntityListener   |   Listen to Entity events
IComponentListener   |   Listen to component events
ISystemListener   |   Listen to system events

## The renderer
The built in software renderer in Nimbus comes with a few different features. Let’s quickly walk through how it works.

The renderer has draw functions for:
- Images
- Text
- Light
- Rectangles (filled and non-filled)

The renderer stores camera coordinates that determine where in the world it it is currently drawing. This is used for view frustum and clipping. The renderer will never draw anything outside of the camera view. By default the camera is set to "0, 0".

After all render systems have been executed the renderer will do post processing work. This includes alpha blending and light rendering. These operations have to be carried out by the renderer in a specific order. In some engines this is left up to the user to handle and ensure, but it is by default handled by the renderer itself in Nimbus (although you can also certainly tell the renderer do whatever you want).

The renderer stores a z buffer that determines what “height” pixels are rendered at. Pixels at a higher z buffer will always take precedence. 

There is a light map and a light block map. The light map holds information about where light should be applied, and the light block how the raycasting should be applied. An ambient colour (light, essentially) can be set in the renderer and will be applied everywhere.

Alpha mod controls if the renderer should apply additional alpha channel modifications to what it is rendering. 1f means no change, 0f means fully transparent.

## Utilities
There are a few additional utilities that Nimbus comes with:
Utility | Description
--- | ---
Quad tree   |   Can be used for efficient collision detection
Simplex noise generation   |   Can be used for procedurally generating noise
Font generator   |   This is used to generate “NimbusFont”s that can be handed to the renderer when rendering text.

## Examples
### Platformer
Platformer is an example of how a game can be implemented using the Nimbus engine. It’s pretty cool, so please check it out!
![Screenshot 1](/screenshot1.png?raw=true)
![Screenshot 2](/screenshot2.png?raw=true)

## Roadmap moving forward
This is a list of issues that I would like to focus on moving forward.
- Improve performance (there is quite a lot that can be done!!)
- Add entity and component pooling
- Improve diffuse lighting
- Add some form of configurable ambiance bias to the lighting system
- Implement scaling
- Implement rotation
- Implement debugging tools
- Create a logger
- Better documentation (thorough javadoc)
- Collision detection for other shapes than rectangles
- Create a way to save/load component states to/from a file
- Build a top down game example

## Special Thanks
This project has been quite the journey for me. I started out knowing very little about ECS, game development or the innards of software renderers. I am immensely grateful to all those people out there that write long detailed blog posts, make YouTube videos and make content available for others. A massive thank you!! It is my hope that perhaps this project and source code can help others as well!

I also want to thank Moé Takemura for the nice graphics! :)