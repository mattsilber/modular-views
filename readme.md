# modular-views

[![Download](https://img.shields.io/maven-central/v/com.guardanis/modular-views)](https://search.maven.org/artifact/com.guardanis/modular-views)

I'm tired of creating custom Views when I want to draw something on the Canvas and/or handling touch events. This is an attempt at solving that by registering modularized components that can each handle those delegations.

# Installation

```
    repositories {
        mavenCentral()
    }

    dependencies {
        compile('com.guardanis:modular-views:1.0.8')
    }
```

# ModularView

A ModularView (or one of the other base View classes) contains a ModularController and contains helper methods for registering/unregistering the ViewModule components. 

### Implemented ModularView types
* ModularView
* ModularImageView
* ModularTextView

# ViewModule

The base ViewModule is what child components must extend in order to be implemented. The ModularController then handles the delegation of touch and drawing events to all of its children.

The default behavior of the ModularController for errors during **onTouch(View, MotionEvent)** and **draw(Canvas)** is to log the Exception and continue. My personal opinion is that I'd rather a rendering failure than a flat out crash by some meaningless edgecase.

### AnimationModule

The AnimationModule is a special-case ViewModule that implements Runnable and handles most of heavy lifting for the animation. Children must override **onAnimationUpdate()** to change the data, and call **onAnimationCompleted()** once the animation has finished.

Calling **start()** or **start(AnimationEventListener)** will start the animation Thread. The AnimationEventListener you pass in with the call to start will be triggered on the View's thread during onAnimationCompleted().

##### ShatterModule

The ShatterModule is an example AnimationModule to showcase what can actually be done with modularization.

    ModularImageView v = (ModularImageView)findViewById(R.id.some_modular_view);
    v.registerModule(new ShatterModule());

    v.getModule(ShatterModule.class).start();

### Drawing order

By default, there is no ordering to the drawing calls except that AnimationModules are drawn *after* ViewModules. If you want to explicitly set the ordering (including mixing AnimationModules behind ViewModule layers), you need to call *setDrawingPrioritiesOrder(List<Class>)* or *setDrawingPrioritiesOrder(Class[])* on the controller (or use the helper methods of the same name in the base classes). The order entered should be from first drawn (background) to last drawn (foreground).

### Moved to MavenCentral

As of version 1.0.8, modular-views will be hosted on MavenCentral. Versions 1.0.7 and below will remain on JCenter.

## Limitations / To Do's

* Implement more ModularControllers for other types of Views/layouts
* Implement prioritizing the touch events
